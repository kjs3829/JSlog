package jslog.post.application;

import jslog.comment.domain.Comment;
import jslog.comment.repository.CommentRepository;
import jslog.likes.domain.Likes;
import jslog.likes.repository.LikesRepository;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.repository.MemberRepository;
import jslog.post.PostPageResponse;
import jslog.post.SearchCondition;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.post.repository.PostRepository;
import jslog.post.ui.dto.*;
import jslog.postWithTag.domain.PostWithTag;
import jslog.postWithTag.repository.PostWithTagRepository;
import jslog.tag.MemberTag;
import jslog.tag.domain.Tag;
import jslog.tag.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired PostService postService;
    @Autowired PostRepository postRepository;
    @Autowired TagRepository tagRepository;
    @Autowired PostWithTagRepository postWithTagRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired LikesRepository likesRepository;

    private final int PAGE_SIZE = 5;

    @Test
    @DisplayName("조건 없는 게시글 목록 조회 성공")
    void no_condition_getPostPageResponse() {
        //given
        Member member = Member.create(any(),anyString(),any());
        memberRepository.save(member);

        Post post = Post.builder().author(member).build();
        postRepository.save(post);

        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.none,
                null,
                null,
                postRepository.findByAuthorIdOrderByCreatedDateDesc(member.getId(), PageRequest.of(0, PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(member.getId(), null, null, null);

        //then
        assertThat(postPageResponse).isEqualTo(find);
    }

    @Test
    @DisplayName("페이지 번호 조건으로 게시글 목록 조회 성공")
    void page_number_condition_getPostPageResponse() {
        //given
        Member member = Member.create(any(),anyString(),any());
        memberRepository.save(member);

        Post post = Post.builder().author(member).build();
        postRepository.save(post);
        int p = 1;

        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.none,
                null,
                null,
                postRepository.findByAuthorIdOrderByCreatedDateDesc(member.getId(), PageRequest.of(p-1, PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(member.getId(), null, null, p);

        //then
        assertThat(postPageResponse).isEqualTo(find);
    }

    @Test
    @DisplayName("태그 이름 조건으로 게시글 목록 조회 성공")
    void tag_name_condition_getPostPageResponse() {
        //given
        Member member = Member.create(any(),anyString(),any());
        memberRepository.save(member);

        Post post = Post.builder().author(member).build();
        postRepository.save(post);

        Tag tag = new Tag("테스트태그");
        tagRepository.save(tag);

        PostWithTag postWithTag = new PostWithTag(post,tag);
        postWithTagRepository.save(postWithTag);

        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.tag,
                null,
                "테스트태그",
                postRepository.findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(member.getId(), "테스트태그",PageRequest.of(0,PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(member.getId(), null, "테스트태그", null);

        //then
        assertThat(postPageResponse).isEqualTo(find);
    }

    @Test
    @DisplayName("태그 이름과 페이지 번호 조건으로 게시글 목록 조회 성공")
    void tag_name_and_page_number_condition_getPostPageResponse() {
        //given
        Member member = Member.create(any(),anyString(),any());
        memberRepository.save(member);

        Post post = Post.builder().author(member).build();
        postRepository.save(post);

        Tag tag = new Tag("테스트태그");
        tagRepository.save(tag);

        PostWithTag postWithTag = new PostWithTag(post,tag);
        postWithTagRepository.save(postWithTag);
        int pageNumber = 1;
        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.tag,
                null,
                "테스트태그",
                postRepository.findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(member.getId(), "테스트태그",PageRequest.of(pageNumber-1, PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(member.getId(), null, "테스트태그", pageNumber);

        //then
        assertThat(postPageResponse).isEqualTo(find);
    }

    @Test
    @DisplayName("제목 검색 조건으로 게시글 목록 조회 성공")
    void q_condition_getPostPageResponse() {
        //given
        Member member = Member.create(any(),anyString(),any());
        memberRepository.save(member);

        Post post = Post.builder().author(member).title("제목입니다.").build();
        postRepository.save(post);

        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.q,
                "제",
                null,
                postRepository.findByAuthorIdAndTitleContainingOrderByCreatedDateDesc(member.getId(), "제",PageRequest.of(0, PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(member.getId(), "제", null, null);

        //then
        assertThat(postPageResponse).isEqualTo(find);
    }

    @Test
    @DisplayName("제목 검색, 페이지 번호 조건으로 게시글 목록 조회 성공")
    void q_and_page_number_condition_getPostPageResponse() {
        //given
        Member member = Member.create(any(),anyString(),any());
        memberRepository.save(member);

        Post post = Post.builder().title("제목입니다.").author(member).build();
        postRepository.save(post);

        int p = 1;
        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.q,
                "입니",
                null,
                postRepository.findByAuthorIdAndTitleContainingOrderByCreatedDateDesc(member.getId(),"입니", PageRequest.of(p-1, PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(member.getId(), "입니", null, p);

        //then
        assertThat(postPageResponse).isEqualTo(find);
    }

    @Test
    @DisplayName("이전, 이후 게시글이 없는 게시글을 조회 성공")
    void none_prev_next_post_getPostReadResponse() {
        //given
        Member member = Member.create(null, null, null);
        memberRepository.save(member);

        Post post = Post.builder().author(member).customUrl(CustomUrl.create(anyString())).build();
        postRepository.save(post);

        Tag tag = new Tag("testTag1");
        tagRepository.save(tag);

        PostWithTag postWithTag = new PostWithTag(post, tag);
        postWithTagRepository.save(postWithTag);

        PostResponse givenPostResponse = PostResponse.create(PostDto.create(post));

        //when
        PostReadResponse postReadResponse = postService.getPostReadResponse(member.getId(), post.getStringUrl(), null);

        //then
        assertThat(postReadResponse.getPostResponse()).isEqualTo(givenPostResponse);
        assertThat(postReadResponse.getPrevPostResponse()).isEqualTo(null);
        assertThat(postReadResponse.getNextPostResponse()).isEqualTo(null);
    }

    @Test
    @DisplayName("이전 게시글이 있는 게시글을 조회 성공")
    void has_prev_post_getPostReadResponse() {
        //given
        Member member = Member.create(null, null, null);
        memberRepository.save(member);

        Post prevPost = Post.builder().author(member).customUrl(CustomUrl.create("이전게시글")).build();
        postRepository.save(prevPost);

        Post post = Post.builder().author(member).customUrl(CustomUrl.create("다음게시글")).build();
        post.setBeforePostId(prevPost.getId());
        postRepository.save(post);

        Tag tag = new Tag("testTag1");
        tagRepository.save(tag);

        PostWithTag postWithTag = new PostWithTag(post, tag);
        postWithTagRepository.save(postWithTag);

        PostResponse givenPostResponse = PostResponse.create(PostDto.create(post));
        PrevNextPostResponse prevPostResponse = PrevNextPostResponse.create(PostDto.create(prevPost));

        //when
        PostReadResponse postReadResponse = postService.getPostReadResponse(member.getId(), post.getStringUrl(), null);

        //then
        assertThat(postReadResponse.getPostResponse()).isEqualTo(givenPostResponse);
        assertThat(postReadResponse.getPrevPostResponse()).isEqualTo(prevPostResponse);
        assertThat(postReadResponse.getNextPostResponse()).isEqualTo(null);
    }

    @Test
    @DisplayName("이후 게시글이 있는 게시글을 조회 성공")
    void has_next_post_getPostReadResponse() {
        //given
        Member member = Member.create(null, null, null);
        memberRepository.save(member);

        Post post = Post.builder().author(member).customUrl(CustomUrl.create("이전게시글")).build();
        postRepository.save(post);

        Post nextPost = Post.builder().author(member).customUrl(CustomUrl.create("이후게시글")).build();
        postRepository.save(nextPost);
        post.setNextPostId(nextPost.getId());

        Tag tag = new Tag("testTag1");
        tagRepository.save(tag);

        PostWithTag postWithTag = new PostWithTag(post, tag);
        postWithTagRepository.save(postWithTag);

        PostResponse givenPostResponse = PostResponse.create(PostDto.create(post));
        PrevNextPostResponse nextPostResponse = PrevNextPostResponse.create(PostDto.create(nextPost));

        //when
        PostReadResponse postReadResponse = postService.getPostReadResponse(member.getId(), post.getStringUrl(), null);

        //then
        assertThat(postReadResponse.getPostResponse()).isEqualTo(givenPostResponse);
        assertThat(postReadResponse.getPrevPostResponse()).isEqualTo(null);
        assertThat(postReadResponse.getNextPostResponse()).isEqualTo(nextPostResponse);
    }

    @Test
    @DisplayName("첫 게시글을 작성 성공")
    void none_prev_post_writePost() {
        //given
        PostWriteRequest postWriteRequest = new PostWriteRequest(anyString(), anyString(), anyString(), anyString(), anyString());

        Member tester1 = Member.create(any(), anyString(), any());
        memberRepository.save(tester1);

        LoginMember loginMember = LoginMember.createMember(tester1);

        //when
        Post createdPost = postService.writePost(postWriteRequest, loginMember);

        //then
        assertThat(postWriteRequest.getTitle()).isEqualTo(createdPost.getTitle());
        assertThat(postWriteRequest.getContent()).isEqualTo(createdPost.getContent());
        assertThat(CustomUrl.create(postWriteRequest.getUrl()).getUrl()).isEqualTo(createdPost.getStringUrl());
        assertThat(postWriteRequest.getTags()).isEqualTo(createdPost.getStringUrl());
        assertThat(postWriteRequest.getPreview()).isEqualTo(createdPost.getPreview());
        assertThat(createdPost.getBeforePostId()).isEqualTo(null);
        assertThat(createdPost.getNextPostId()).isEqualTo(null);
    }

    @Test
    @DisplayName("첫 글이 아닌 이전 글이 존재하는 작가가 새로운 게시글을 작성 성공")
    void has_prev_post_writePost() {
        //given
        Member member = Member.create(any(),anyString(),any());
        memberRepository.save(member);

        Post prevPost = Post.builder().author(member).build();
        postRepository.save(prevPost);

        Tag tag = new Tag("테스트태그");
        tagRepository.save(tag);

        PostWithTag postWithTag = new PostWithTag(prevPost,tag);
        postWithTagRepository.save(postWithTag);

        PostWriteRequest postWriteRequest = new PostWriteRequest(anyString(), anyString(), anyString(), anyString(), anyString());

        LoginMember loginMember = LoginMember.createMember(member);

        //when
        Post createdPost = postService.writePost(postWriteRequest, loginMember);

        //then
        assertThat(postWriteRequest.getTitle()).isEqualTo(createdPost.getTitle());
        assertThat(postWriteRequest.getContent()).isEqualTo(createdPost.getContent());
        assertThat(CustomUrl.create(postWriteRequest.getUrl()).getUrl()).isEqualTo(createdPost.getStringUrl());
        assertThat(postWriteRequest.getTags()).isEqualTo(createdPost.getStringUrl());
        assertThat(postWriteRequest.getPreview()).isEqualTo(createdPost.getPreview());
        assertThat(createdPost.getBeforePostId()).isEqualTo(prevPost.getId());
        assertThat(createdPost.getNextPostId()).isEqualTo(null);
        assertThat(prevPost.getNextPostId()).isEqualTo(createdPost.getId());
    }

    @Test
    @DisplayName("게시글 작성자가 이전 게시글이 있는 게시글을 삭제")
    void has_prev_post_deletePost() {
        //given
        Member member = Member.create(null, "테스터1", null);
        memberRepository.save(member);

        Post prevPost = Post.builder().author(member).build();
        postRepository.save(prevPost);

        Post post = Post.builder().author(member).build();
        post.setBeforePostId(prevPost.getId());
        postRepository.save(post);
        prevPost.setNextPostId(post.getId());

        Tag tag = new Tag(null);
        tagRepository.save(tag);

        PostWithTag postWithTag = new PostWithTag(post, tag);
        postWithTagRepository.save(postWithTag);

        Comment comment = Comment.create(post, member, null);
        commentRepository.save(comment);

        Likes likes = Likes.create(member, post);
        likesRepository.save(likes);

        LoginMember loginMember = LoginMember.createMember(member);

        //when
        postService.deletePost(loginMember, post.getId());

        //then
        assertThat(prevPost.getNextPostId()).isEqualTo(null);
        assertThat(postRepository.findById(post.getId())).isEqualTo(Optional.empty());
        assertThat(postWithTagRepository.findById(postWithTag.getId())).isEqualTo(Optional.empty());
        assertThat(commentRepository.findById(comment.getId())).isEqualTo(Optional.empty());
        assertThat(likesRepository.findById(likes.getId())).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("게시글 작성자가 이후 게시글이 있는 게시글을 삭제")
    void has_next_post_deletePost() {
        //given
        Member member = Member.create(null, "테스터1", null);
        memberRepository.save(member);

        Post post = Post.builder().author(member).build();
        postRepository.save(post);

        Post nextPost = Post.builder().author(member).build();
        postRepository.save(nextPost);
        post.setNextPostId(nextPost.getId());
        nextPost.setBeforePostId(post.getId());

        Tag tag = new Tag(null);
        tagRepository.save(tag);

        PostWithTag postWithTag = new PostWithTag(post, tag);
        postWithTagRepository.save(postWithTag);

        Comment comment = Comment.create(post, member, null);
        commentRepository.save(comment);

        Likes likes = Likes.create(member, post);
        likesRepository.save(likes);

        LoginMember loginMember = LoginMember.createMember(member);

        //when
        postService.deletePost(loginMember, post.getId());

        //then
        assertThat(nextPost.getBeforePostId()).isEqualTo(null);
        assertThat(postRepository.findById(post.getId())).isEqualTo(Optional.empty());
        assertThat(postWithTagRepository.findById(postWithTag.getId())).isEqualTo(Optional.empty());
        assertThat(commentRepository.findById(comment.getId())).isEqualTo(Optional.empty());
        assertThat(likesRepository.findById(likes.getId())).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("MemberTag 조회")
    void getMemberTags() {
        //given
        Member member = Member.create(null, "테스터1", null);
        memberRepository.save(member);

        Post post = Post.builder().author(member).build();
        postRepository.save(post);

        Tag tag = new Tag("테스트태그");
        tagRepository.save(tag);

        PostWithTag postWithTag = new PostWithTag(post, tag);
        postWithTagRepository.save(postWithTag);

        List<MemberTag> m = new ArrayList<>(Arrays.asList(new MemberTag(member.getId(),1L,"테스트태그")));

        //when
        List<MemberTag> memberTags = postService.getMemberTags(member.getId());

        //then
        assertThat(memberTags).isEqualTo(m);
    }

}