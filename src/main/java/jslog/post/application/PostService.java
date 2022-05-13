package jslog.post.application;

import jslog.comment.domain.Comment;
import jslog.comment.repository.CommentRepository;
import jslog.comment.ui.dto.CommentDto;
import jslog.likes.application.LikesService;
import jslog.likes.domain.Likes;
import jslog.likes.repository.LikesRepository;
import jslog.member.auth.exception.UnauthorizedException;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.PostPageViewer;
import jslog.post.SearchCondition;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.postWithTag.domain.PostWithTag;
import jslog.post.repository.PostRepository;
import jslog.post.ui.dto.PostEditForm;
import jslog.postWithTag.repository.PostWithTagRepository;
import jslog.tag.MemberTag;
import jslog.tag.domain.Tag;
import jslog.post.ui.dto.PostReadForm;
import jslog.post.ui.dto.PostWriteForm;
import jslog.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * TODO : TAG Split 객체 구현, edit시 원래 url로 수정이 불가능한 이슈 수정 필요
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
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final LikesService likesService;

    public PostPageViewer getPageSelector(long authorId, String q, String tag, Integer p) {
        PageRequest pr = PageRequest.of(p!=null?p-1:0, PAGE_SIZE);
        if (q != null && !q.equals("")) {
            return new PostPageViewer(SearchCondition.q,
                    q,
                    null,
                    postRepository.findByAuthorIdAndTitleContainingOrderByCreatedDateDesc(authorId,q, pr),
                    PAGE_SIZE);
        }
        else if (tag != null && !tag.equals("")) {
            return new PostPageViewer(SearchCondition.tag,
                    null,
                    tag,
                    postRepository.findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(authorId, tag, pr),
                    PAGE_SIZE);
        }
        return new PostPageViewer(SearchCondition.none,
                null,
                null,
                postRepository.findByAuthorIdOrderByCreatedDateDesc(authorId, pr),
                PAGE_SIZE);
    }

    public List<MemberTag> getMemberTags(Long authorId) {
        return tagRepository.getMemberTags(authorId);
    }

    public PostReadForm getPostReadForm(Long authorId, String url, LoginMember loginMember) {
        Post post = postRepository.findByAuthorIdAndCustomUrlUrl(authorId, url).orElseThrow(NoSuchElementException::new);

        List<Comment> comments = commentRepository.findByAuthorIdAndPostId(authorId, post.getId());


        //TODO : postDto로 바꿔줘야함
        PostReadForm readForm = new PostReadForm(post,
                comments.stream().map(CommentDto::create).collect(Collectors.toList()),
                likesService.createLikesResponse(loginMember, post.getId()));

        if (post.hasBeforePost()) {
            Post bp = postRepository.findById(post.getBeforePostId()).orElseThrow(NoSuchElementException::new);
            readForm.setBeforePostTitle(bp.getTitle());
            readForm.setBeforePostUrl(bp.getStringUrl());
            readForm.setBeforePostAuthorId(bp.getAuthor().getId());
        }
        if (post.hasNextPost()) {
            Post np = postRepository.findById(post.getNextPostId()).orElseThrow(NoSuchElementException::new);
            readForm.setNextPostTitle(np.getTitle());
            readForm.setNextPostUrl(np.getStringUrl());
            readForm.setNextPostAuthorId(np.getAuthor().getId());
        }

        return readForm;
    }

    @Transactional
    public Post createPost(PostWriteForm form, LoginMember loginMember) {

        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(NoSuchElementException::new);

        Post beforePost = getRecentPost(member.getId());

        Post post = Post.builder().author(member)
                .content(form.getContent())
                .title(form.getTitle())
                .customUrl(new CustomUrl(form.getUrl()))
                .preview(form.getPreview())
                .beforePostId(beforePost != null ? beforePost.getId() : null)
                .build();
        postRepository.save(post);

        createTags(form.getTags(), post);

        if (beforePost != null) beforePost.setNextPostId(post.getId());

        return post;
    }

    @Transactional
    public boolean updatePost(PostEditForm form, LoginMember loginMember) {
        Post post = postRepository.findById(form.getId()).orElseThrow(NoSuchElementException::new);

        CustomUrl customUrl = CustomUrl.create(form.getUrl());
        if (!post.getStringUrl().equals(customUrl.getUrl()) && isDuplicatedUrl(loginMember.getId(), customUrl.getUrl())) return false;

        checkAuthorization(loginMember, post.getAuthor().getId());
        form.setUrl(customUrl.getUrl());
        post.edit(form);

        // Tag Delete (게시글에 등록된 태그 전체를 삭제하고 전부 새로 생성하는 전략)
        for (PostWithTag pwt : post.getPostWithTags()) postWithTagRepository.delete(pwt);
        createTags(form.getTags(), post);

        return true;
    }

    @Transactional
    public Long delete(LoginMember loginMember, Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);

        checkAuthorization(loginMember, findPost.getAuthor().getId());

        // 이전포스트, 다음포스트 재배치
        if (findPost.getBeforePostId() != null) {
            Post bp = postRepository.findById(findPost.getBeforePostId()).orElseThrow(NoSuchElementException::new);
            if (findPost.getNextPostId() != null) bp.setNextPostId(findPost.getNextPostId());
            else bp.setNextPostId(null);
        }
        if (findPost.getNextPostId() != null) {
            Post np = postRepository.findById(findPost.getNextPostId()).orElseThrow(NoSuchElementException::new);
            if (findPost.getBeforePostId() != null) np.setBeforePostId(findPost.getBeforePostId());
            else np.setBeforePostId(null);
        }

        for (Comment comment : findPost.getComments()) {
            commentRepository.delete(comment);
        }

        for (PostWithTag postWithTag : findPost.getPostWithTags()) {
            postWithTagRepository.delete(postWithTag);
        }

        postRepository.delete(findPost);

        return findPost.getAuthor().getId();
    }

    public boolean isDuplicatedUrl(Long id, String customUrl) {
        return customUrl.equals("") || postRepository.findByAuthorIdAndCustomUrlUrl(id, customUrl).orElse(null) != null;
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
                    Tag findOrCreateTag = tagRepository.findTagByName(tagName).orElseGet(() -> new Tag(tagName));
                    tagRepository.save(findOrCreateTag);
                    postWithTagRepository.save(new PostWithTag(post, findOrCreateTag));
                }
            }
        }
    }

}
