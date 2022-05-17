package jslog.post.application;

import jslog.comment.application.CommentService;
import jslog.likes.application.LikesService;
import jslog.member.auth.exception.UnauthorizedException;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.PostPageResponse;
import jslog.post.SearchCondition;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.post.ui.dto.*;
import jslog.postWithTag.domain.PostWithTag;
import jslog.post.repository.PostRepository;
import jslog.postWithTag.repository.PostWithTagRepository;
import jslog.tag.MemberTag;
import jslog.tag.domain.Tag;
import jslog.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * TODO : TAG Split 객체 구현
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final static int PAGE_SIZE = 5;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostWithTagRepository postWithTagRepository;
    private final CommentService commentService;
    private final LikesService likesService;

    public PostPageResponse getPostPageResponse(long authorId, String q, String tag, Integer p) {
        PageRequest pr = PageRequest.of(p!=null?p-1:0, PAGE_SIZE);

        if (q == null && tag == null) {
            return new PostPageResponse(SearchCondition.none,
                    null,
                    null,
                    postRepository.findByAuthorIdOrderByCreatedDateDesc(authorId, pr));
        } else if (q == null) {
            return new PostPageResponse(SearchCondition.tag,
                    null,
                    tag,
                    postRepository.findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(authorId, tag, pr));
        }
        return new PostPageResponse(SearchCondition.q,
                q,
                null,
                postRepository.findByAuthorIdAndTitleContainingOrderByCreatedDateDesc(authorId,q, pr));
    }

    public List<MemberTag> getMemberTags(Long authorId) {
        return postWithTagRepository.getMemberTags(authorId);
    }


    public PostReadResponse getPostReadResponse(Long authorId, String url, LoginMember loginMember) {
        Post post = postRepository.findByAuthorIdAndCustomUrlUrl(authorId, url).orElseThrow(NoSuchElementException::new);

        PrevNextPostResponse prevPostResponse = null, nextPostResponse = null;

        if (post.hasBeforePost()) {
            Post prevPost = postRepository.findById(post.getBeforePostId()).orElseThrow(NoSuchElementException::new);
            prevPostResponse = PrevNextPostResponse.create(PostDto.create(prevPost));
        }
        if (post.hasNextPost()) {
            Post nextPost = postRepository.findById(post.getNextPostId()).orElseThrow(NoSuchElementException::new);
            nextPostResponse = PrevNextPostResponse.create(PostDto.create(nextPost));
        }

        return new PostReadResponse(PostResponse.create(PostDto.create(post)),
                prevPostResponse,
                nextPostResponse,
                commentService.createCommentResponse(post.getId()),
                likesService.createLikesResponse(loginMember, post.getId())
        );
    }

    @Transactional
    public Post writePost(PostWriteRequest postWriteRequest, LoginMember loginMember) {

        Member author = memberRepository.findById(loginMember.getId()).orElseThrow(NoSuchElementException::new);

        Post post = Post.builder().author(author)
                .content(postWriteRequest.getContent())
                .title(postWriteRequest.getTitle())
                .customUrl(new CustomUrl(postWriteRequest.getUrl()))
                .preview(postWriteRequest.getPreview())
                .build();

        Post beforePost = getRecentPost(author.getId());

        postRepository.save(post);

        if (beforePost != null) {
            post.setBeforePostId(beforePost.getId());
            beforePost.setNextPostId(post.getId());
        }

        createTags(postWriteRequest.getTags(), post);

        return post;
    }

    @Transactional
    public boolean updatePost(PostEditRequest form, LoginMember loginMember) {
        Post post = postRepository.findById(form.getId()).orElseThrow(NoSuchElementException::new);

        CustomUrl customUrl = CustomUrl.create(form.getUrl());
        Post findPost = postRepository.findByAuthorIdAndCustomUrlUrl(loginMember.getId(), customUrl.getUrl()).orElse(null);
        if (findPost != null && !findPost.getId().equals(post.getId())) return false;

        checkAuthorization(loginMember, post.getAuthor().getId());
        form.setUrl(customUrl.getUrl());
        post.edit(form);

        // Tag Delete (게시글에 등록된 태그 전체를 삭제하고 전부 새로 생성하는 전략)
        for (PostWithTag pwt : post.getPostWithTags()) postWithTagRepository.delete(pwt);
        createTags(form.getTags(), post);

        return true;
    }

    @Transactional
    public Long deletePost(LoginMember loginMember, Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);

        checkAuthorization(loginMember, findPost.getAuthor().getId());

        // 이전포스트, 다음포스트 재배치
        if (findPost.getBeforePostId() != null) {
            Post bp = postRepository.findById(findPost.getBeforePostId()).orElseThrow(NoSuchElementException::new);
            bp.setNextPostId(findPost.getNextPostId());
        }
        if (findPost.getNextPostId() != null) {
            Post np = postRepository.findById(findPost.getNextPostId()).orElseThrow(NoSuchElementException::new);
            np.setBeforePostId(findPost.getBeforePostId());
        }

        commentService.deleteCommentsByPostId(findPost.getId());

        for (PostWithTag postWithTag : findPost.getPostWithTags()) {
            postWithTagRepository.delete(postWithTag);
        }

        likesService.deleteLikesByPostId(findPost.getId());

        postRepository.delete(findPost);

        return findPost.getAuthor().getId();
    }

    public boolean isDuplicatedUrl(Long id, String customUrl) {
        return postRepository.findByAuthorIdAndCustomUrlUrl(id, customUrl).orElse(null) != null;
    }

    private Post getRecentPost(Long authorId) {
        return postRepository.findSliceByAuthorIdOrderByCreatedDateDesc(authorId, PageRequest.of(0, 1))
                .get()
                .findFirst()
                .orElse(null);
    }

    private void checkAuthorization(LoginMember loginMember, Long authorizedMemberId) {
        if(!authorizedMemberId.equals(loginMember.getId()) && loginMember.getMemberRole() != MemberRole.ADMIN)
            throw new UnauthorizedException();
    }

    private void createTags(String stringTag, Post post) {
        if (stringTag != null) {
            String tagString = stringTag.trim();
            if (!tagString.equals("")) {
                String[] tags = tagString.split(",");
                for (String tag : tags) {
                    String tagName = tag.trim();
                    Tag findTag = tagRepository.findTagByName(tagName).orElse(null);
                    if (findTag == null) {
                        findTag = new Tag(tagName);
                        tagRepository.save(findTag);
                    }
                    postWithTagRepository.save(new PostWithTag(post, findTag));
                }
            }
        }
    }

}
