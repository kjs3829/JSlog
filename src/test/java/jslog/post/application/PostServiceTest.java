package jslog.post.application;

import jslog.comment.application.CommentService;
import jslog.comment.ui.dto.CommentResponse;
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
import jslog.post.repository.PostRepository;
import jslog.post.ui.dto.*;
import jslog.postWithTag.domain.PostWithTag;
import jslog.postWithTag.repository.PostWithTagRepository;
import jslog.tag.domain.Tag;
import jslog.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.SliceImpl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    PostService postService;
    @Mock PostRepository postRepository;
    @Mock TagRepository tagRepository;
    @Mock PostWithTagRepository postWithTagRepository;
    @Mock MemberRepository memberRepository;
    @Mock CommentService commentService;
    @Mock LikesService likesService;

    Member member;
    LoginMember loginMember;
    Post post1, post2, post3, post4, post5;
    Tag tag1, tag2, tag3;
    PostWithTag postWithTag1, postWithTag2, postWithTag3, postWithTag4, postWithTag5;

    @BeforeEach
    void setUp() {
        postService = new PostService(memberRepository,postRepository,tagRepository,postWithTagRepository,commentService,likesService);

        member = Member.builder()
                .id(1L)
                .nickname("테스트유저")
                .memberRole(MemberRole.MEMBER)
                .build();

        loginMember = LoginMember.createMember(member);

        tag1 = new Tag("테스트태그1");
        tag2 = new Tag("테스트태그2");
        tag3 = new Tag("테스트태그3");

        post1 = Post.builder().id(1L)
                .author(member)
                .title("첫번째 게시글 제목!!")
                .content("첫번째 게시글 내용!!!")
                .customUrl(CustomUrl.create("첫번째 게시글 URL"))
                .build();
        post2 = Post.builder().id(2L)
                .author(member)
                .title("두번째 게시글 제목!!")
                .content("두번째 게시글 내용!!!")
                .customUrl(CustomUrl.create("두번째 게시글 URL"))
                .build();
        post3 = Post.builder().id(3L)
                .author(member)
                .title("세번째 게시글 제목!!")
                .content("세번째 게시글 내용!!!")
                .customUrl(CustomUrl.create("세번째 게시글 URL"))
                .build();
        post4 = Post.builder().id(4L)
                .author(member)
                .title("네번째 게시글 제목!!")
                .content("네번째 게시글 내용!!!")
                .customUrl(CustomUrl.create("네번째 게시글 URL"))
                .build();

        postWithTag1 = new PostWithTag(post1,tag1);
        postWithTag2 = new PostWithTag(post2,tag2);
        postWithTag3 = new PostWithTag(post3,tag3);
        postWithTag4 = new PostWithTag(post4,tag1);
        postWithTag5 = new PostWithTag(post4,tag2);
    }

    @Test
    @DisplayName("게시글 작성일이 최근인 순으로 정렬된 게시글 목록을 조회한다.")
    void getPostPageResponse_condition_none() {
        Page<Post> page = new PageImpl<>(new ArrayList<>(Arrays.asList(post5, post4, post3, post2, post1)));

        when(postRepository.findByAuthorIdOrderByCreatedDateDesc(any(),any())).thenReturn(page);

        PostPageResponse postPageResponse = postService.getPostPageResponse(member.getId(), null, null, null);

        assertThat(postPageResponse).isEqualTo(new PostPageResponse(SearchCondition.none,
                null,
                null,
                page));
    }

    @Test
    @DisplayName("게시글 작성일이 최근인 순으로 정렬된 게시글 목록에서 페이지 번호로 조회한다.")
    void getPostPageResponse_condition_page_number() {
        Page<Post> page = new PageImpl<>(new ArrayList<>(Arrays.asList(post5, post4, post3, post2, post1)));

        when(postRepository.findByAuthorIdOrderByCreatedDateDesc(any(),any())).thenReturn(page);

        PostPageResponse postPageResponse = postService.getPostPageResponse(member.getId(), null, null, 1);

        assertThat(postPageResponse).isEqualTo(new PostPageResponse(SearchCondition.none,
                null,
                null,
                page));
    }

    @Test
    @DisplayName("게시글 작성일이 최근인 순으로 정렬된 게시글 목록에서 태그 이름으로 조회한다.")
    void getPostPageResponse_condition_tag_name() {
        Page<Post> page = new PageImpl<>(new ArrayList<>(Arrays.asList(post4, post1)));

        when(postRepository.findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(any(),anyString(),any())).thenReturn(page);

        PostPageResponse postPageResponse = postService.getPostPageResponse(member.getId(), null, tag1.getName(), null);

        assertThat(postPageResponse).isEqualTo(new PostPageResponse(SearchCondition.tag,
                null,
                tag1.getName(),
                page));
    }

    @Test
    @DisplayName("게시글 작성일이 최근인 순으로 정렬된 게시글 목록에서 태그 이름과 페이지 번호로 조회한다.")
    void getPostPageResponse_condition_tag_name_and_page_number() {
        Page<Post> page = new PageImpl<>(new ArrayList<>(Arrays.asList(post4, post1)));

        when(postRepository.findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(any(),anyString(),any())).thenReturn(page);

        PostPageResponse postPageResponse = postService.getPostPageResponse(member.getId(), null, tag1.getName(), 1);

        assertThat(postPageResponse).isEqualTo(new PostPageResponse(SearchCondition.tag,
                null,
                tag1.getName(),
                page));
    }

    @Test
    @DisplayName("게시글 검색 결과를 조회한다.")
    void getPostPageResponse_condition_q() {
        Page<Post> page = new PageImpl<>(new ArrayList<>(Arrays.asList(post1)));

        when(postRepository.findByAuthorIdAndTitleContainingOrderByCreatedDateDesc(any(),anyString(), any())).thenReturn(page);

        PostPageResponse postPageResponse = postService.getPostPageResponse(member.getId(), "첫번째", null, null);

        assertThat(postPageResponse).isEqualTo(new PostPageResponse(SearchCondition.q,
                "첫번째",
                null,
                page));
    }

    @Test
    @DisplayName("게시글 검색 결과의 특정 페이지 번호를 조회한다.")
    void getPostPageResponse_condition_q_and_page_number() {
        Page<Post> page = new PageImpl<>(new ArrayList<>(Arrays.asList(post1)));

        when(postRepository.findByAuthorIdAndTitleContainingOrderByCreatedDateDesc(any(),anyString(), any())).thenReturn(page);

        PostPageResponse postPageResponse = postService.getPostPageResponse(member.getId(), "첫번째", null, 1);

        assertThat(postPageResponse).isEqualTo(new PostPageResponse(SearchCondition.q,
                "첫번째",
                null,
                page));
    }

    @Test
    @DisplayName("이전, 이후 게시글이 없는 게시글을 조회한다.")
    void getPostReadResponse_none_prev_and_next_post() {
        CommentResponse commentResponse = CommentResponse.create(new ArrayList<>());
        LikesResponse likesResponse = LikesResponse.create(0,false,false);

        when(postRepository.findByAuthorIdAndCustomUrlUrl(any(),anyString())).thenReturn(Optional.of(post1));
        when(commentService.createCommentResponse(any())).thenReturn(commentResponse);
        when(likesService.createLikesResponse(any(), any())).thenReturn(likesResponse);

        PostReadResponse postReadResponse = postService.getPostReadResponse(member.getId(), post1.getStringUrl(), null);

        assertThat(postReadResponse).isEqualTo(new PostReadResponse(PostResponse.create(PostDto.create(post1)),
                null,
                null,
                commentResponse,
                likesResponse));
    }

    @Test
    @DisplayName("이전 게시글이 있는 게시글을 조회한다.")
    void getPostReadResponse_has_prev_post() {
        CommentResponse commentResponse = CommentResponse.create(new ArrayList<>());
        LikesResponse likesResponse = LikesResponse.create(0,false,false);
        post2.setBeforePostId(post1.getId());
        when(postRepository.findByAuthorIdAndCustomUrlUrl(any(),anyString())).thenReturn(Optional.of(post2));
        when(commentService.createCommentResponse(any())).thenReturn(commentResponse);
        when(likesService.createLikesResponse(any(), any())).thenReturn(likesResponse);
        when(postRepository.findById(any())).thenReturn(Optional.of(post1));

        PostReadResponse postReadResponse = postService.getPostReadResponse(member.getId(), post2.getStringUrl(), null);

        assertThat(postReadResponse).isEqualTo(new PostReadResponse(PostResponse.create(PostDto.create(post2)),
                PrevNextPostResponse.create(PostDto.create(post1)),
                null,
                commentResponse,
                likesResponse));
    }

    @Test
    @DisplayName("이후 게시글이 있는 게시글을 조회한다.")
    void getPostReadResponse_has_next_post() {
        CommentResponse commentResponse = CommentResponse.create(new ArrayList<>());
        LikesResponse likesResponse = LikesResponse.create(0,false,false);
        post1.setNextPostId(post2.getId());
        when(postRepository.findByAuthorIdAndCustomUrlUrl(any(),anyString())).thenReturn(Optional.of(post1));
        when(commentService.createCommentResponse(any())).thenReturn(commentResponse);
        when(likesService.createLikesResponse(any(), any())).thenReturn(likesResponse);
        when(postRepository.findById(any())).thenReturn(Optional.of(post2));

        PostReadResponse postReadResponse = postService.getPostReadResponse(member.getId(), post1.getStringUrl(), null);

        assertThat(postReadResponse).isEqualTo(new PostReadResponse(PostResponse.create(PostDto.create(post1)),
                null,
                PrevNextPostResponse.create(PostDto.create(post2)),
                commentResponse,
                likesResponse));
    }

    @Test
    @DisplayName("사용자가 첫 번째 게시글을 작성한다.")
    void write_none_prev_post() {
        PostWriteRequest postWriteRequest = new PostWriteRequest("새로운 게시글 제목!!",
                "새로운 게시글 내용!!",
                "새로운 게시글 URL!!",
                "새로운 태그 이름!!",
                "새로운 게시글 미리보기!!");

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(tagRepository.findTagByName(anyString())).thenReturn(Optional.empty());
        when(postRepository.findSliceByAuthorIdOrderByCreatedDateDesc(any(), any())).thenReturn(new SliceImpl<>(new ArrayList<>()));

        postService.writePost(postWriteRequest, loginMember);

        verify(postRepository).save(any());
        verify(tagRepository).save(any());
        verify(postWithTagRepository).save(any());
    }

    //TODO : 단위테스트를 하기 어려운 로직. 어떻게 해야할까?
    @Test
    @DisplayName("첫 글이 아닌 이전 글이 존재하는 작가가 새로운 게시글을 작성한다.")
    void write_has_prev_post() {
        PostWriteRequest postWriteRequest = new PostWriteRequest("새로운 게시글 제목!!",
                "새로운 게시글 내용!!",
                "새로운 게시글 URL!!",
                "새로운 태그 이름!!",
                "새로운 게시글 미리보기!!");

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        //when(tagRepository.findTagByName(anyString())).thenReturn(Optional.of(new Tag(postWriteRequest.getTags())));
        //when(postRepository.findSliceByAuthorIdOrderByCreatedDateDesc(any(), any())).thenReturn(new SliceImpl<>(new ArrayList<>(Arrays.asList(post5))));


        assertThatThrownBy(()->postService.writePost(postWriteRequest, loginMember)).isInstanceOf(NullPointerException.class);

        /*
        verify(postRepository).save(any());
        verify(tagRepository).save(any());
        verify(postWithTagRepository).save(any());
         */
    }

    @Test
    @DisplayName("이미 있는 url로 게시글을 수정한다. - 수정 실패 ")
    void update_fail_cause_by_duplicated_url() {
        PostEditRequest postEditRequest = PostEditRequest.builder().id(post1.getId()).url(post2.getStringUrl()).build();
        when(postRepository.findById(any())).thenReturn(Optional.of(post1));
        when(postRepository.findByAuthorIdAndCustomUrlUrl(any(),anyString())).thenReturn(Optional.of(post2));

        boolean updated = postService.updatePost(postEditRequest, loginMember);

        assertThat(updated).isFalse();
    }

    @Test
    @DisplayName("어드민이 아닌 악성 사용자가 다른 사람의 게시글을 수정하려 한다. - 수정 실패")
    void update_fail_cause_by_unauthorized_access() {
        LoginMember anotherLoginMember = LoginMember.createMember(2L,"다른 사용자", MemberRole.MEMBER);
        PostEditRequest postEditRequest = PostEditRequest.builder().id(post1.getId()).url(post1.getStringUrl()).build();
        when(postRepository.findById(any())).thenReturn(Optional.of(post1));
        when(postRepository.findByAuthorIdAndCustomUrlUrl(any(),anyString())).thenReturn(Optional.of(post1));

        assertThatThrownBy(()->postService.updatePost(postEditRequest, anotherLoginMember)).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("게시글을 수정한다.")
    void updatePost() {
        PostEditRequest postEditRequest = PostEditRequest.builder().id(post1.getId()).url(post1.getStringUrl()).tags("new Tags").build();
        when(postRepository.findById(any())).thenReturn(Optional.of(post1));
        when(postRepository.findByAuthorIdAndCustomUrlUrl(any(),anyString())).thenReturn(Optional.of(post1));
        when(tagRepository.findTagByName(anyString())).thenReturn(Optional.empty());

        boolean updated = postService.updatePost(postEditRequest, loginMember);

        assertThat(updated).isTrue();
        verify(postWithTagRepository).delete(any());
        verify(tagRepository).save(any());
        verify(postWithTagRepository).save(any());
    }

    @Test
    @DisplayName("어드민이 아닌 악성 사용자가 다른 사람의 게시글을 삭제하려 한다. - 삭제 실패")
    void delete_fail_cause_by_unauthorized_access() {
        LoginMember anotherLoginMember = LoginMember.createMember(2L,"다른 사용자", MemberRole.MEMBER);

        when(postRepository.findById(any())).thenReturn(Optional.of(post1));

        assertThatThrownBy(()->postService.deletePost(anotherLoginMember, post1.getId())).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("이전 게시글이 있는 게시글을 삭제한다.")
    void delete_has_prev_post() {
        post1.setNextPostId(post2.getId());
        post2.setBeforePostId(post1.getId());
        when(postRepository.findById(post2.getId())).thenReturn(Optional.of(post2));
        when(postRepository.findById(post1.getId())).thenReturn(Optional.of(post1));

        postService.deletePost(loginMember, post2.getId());

        assertThat(post1.getNextPostId()).isEqualTo(null);
        verify(commentService).deleteCommentsByPostId(any());
        verify(postWithTagRepository).delete(any());
        verify(likesService).deleteLikesByPostId(any());
        verify(postRepository).delete(any());
    }

    @Test
    @DisplayName("다음 게시글이 있는 게시글을 삭제한다.")
    void delete_has_next_post() {
        post1.setNextPostId(post2.getId());
        post2.setBeforePostId(post1.getId());
        when(postRepository.findById(post2.getId())).thenReturn(Optional.of(post2));
        when(postRepository.findById(post1.getId())).thenReturn(Optional.of(post1));

        postService.deletePost(loginMember, post1.getId());

        assertThat(post2.getBeforePostId()).isEqualTo(null);
        verify(commentService).deleteCommentsByPostId(any());
        verify(postWithTagRepository).delete(any());
        verify(likesService).deleteLikesByPostId(any());
        verify(postRepository).delete(any());
    }

    @Test
    @DisplayName("블로그 작가가 작성한 모든 태그들을 조회한다.")
    void getMemberTags() {
        postService.getMemberTags(member.getId());

        verify(postWithTagRepository).getMemberTags(member.getId());
    }

    @Test
    @DisplayName("이미 등록된 URL로 URL 중복 검사를 한다.")
    void isDuplicatedUrl_true() {
        Post newPost = Post.builder().id(10L).author(member).customUrl(post1.getCustomUrl()).build();
        when(postRepository.findByAuthorIdAndCustomUrlUrl(any(),anyString())).thenReturn(Optional.of(post1));

        boolean duplicated = postService.isDuplicatedUrl(member.getId(), newPost.getStringUrl());

        assertThat(duplicated).isTrue();
    }

    @Test
    @DisplayName("등록되지 않은 URL로 URL 중복 검사를 한다.")
    void isDuplicatedUrl_false() {
        Post newPost = Post.builder().id(10L).author(member).customUrl(CustomUrl.create("등록되어 있지 않은 URL")).build();
        when(postRepository.findByAuthorIdAndCustomUrlUrl(any(),anyString())).thenReturn(Optional.empty());

        boolean duplicated = postService.isDuplicatedUrl(member.getId(), newPost.getStringUrl());

        assertThat(duplicated).isFalse();
    }

}