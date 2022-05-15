package jslog.post.application;

import jslog.comment.domain.Comment;
import jslog.comment.repository.CommentRepository;
import jslog.comment.ui.dto.CommentResponse;
import jslog.likes.domain.Likes;
import jslog.likes.repository.LikesRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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

    private Member testMember1, testMember2;
    private Post post, post2, post3, post4, post5, post6;
    private Tag tag, tag2;
    private PostWithTag postWithTag1, postWithTag2, postWithTag3, postWithTag4, postWithTag5, postWithTag6;


    @BeforeEach
    void init() {
        testMember1 = Member.create(null, "tester1", MemberRole.MEMBER);
        testMember2 = Member.create(null, "tester2", MemberRole.MEMBER);
        memberRepository.save(testMember1);
        memberRepository.save(testMember2);

        post = Post.builder().author(testMember1)
                .title("제목1")
                .customUrl(CustomUrl.create("1"))
                .build();
        post2 = Post.builder().author(testMember1)
                .title("제목2")
                .customUrl(CustomUrl.create("2"))
                .build();
        post3 = Post.builder().author(testMember1)
                .title("제목3")
                .customUrl(CustomUrl.create("3"))
                .build();
        post4 = Post.builder().author(testMember2)
                .title("제목4")
                .customUrl(CustomUrl.create("4"))
                .build();
        post5 = Post.builder().author(testMember2)
                .title("제목5")
                .customUrl(CustomUrl.create("5"))
                .build();
        post6 = Post.builder().author(testMember2)
                .title("제목6")
                .customUrl(CustomUrl.create("6"))
                .build();
        postRepository.save(post);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);
        postRepository.save(post6);

        tag = new Tag("testTag1");
        tagRepository.save(tag);
        tag2 = new Tag("testTag2");
        tagRepository.save(tag2);

        postWithTag1 = new PostWithTag(post, tag);
        postWithTagRepository.save(postWithTag1);
        postWithTag2 = new PostWithTag(post2, tag);
        postWithTagRepository.save(postWithTag2);
        postWithTag3 = new PostWithTag(post3, tag2);
        postWithTagRepository.save(postWithTag3);
        postWithTag4 = new PostWithTag(post4, tag);
        postWithTagRepository.save(postWithTag4);
        postWithTag5 = new PostWithTag(post5, tag2);
        postWithTagRepository.save(postWithTag5);
        postWithTag6 = new PostWithTag(post5, tag2);
        postWithTagRepository.save(postWithTag6);
    }

    @Test
    @DisplayName("조건 없는 게시글 목록 조회 성공")
    void no_condition_getPostPageResponse() {
        //given
        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.none,
                null,
                null,
                postRepository.findByAuthorIdOrderByCreatedDateDesc(testMember1.getId(), PageRequest.of(0, PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(testMember1.getId(), null, null, null);

        //then
        postPageResponseTest(postPageResponse, find);
    }

    @Test
    @DisplayName("페이지 번호 조건으로 게시글 목록 조회 성공")
    void page_number_condition_getPostPageResponse() {
        //given
        int p = 2;
        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.none,
                null,
                null,
                postRepository.findByAuthorIdOrderByCreatedDateDesc(testMember1.getId(), PageRequest.of(p-1, PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(testMember1.getId(), null, null, p);

        //then
        postPageResponseTest(postPageResponse, find);
    }

    @Test
    @DisplayName("태그 이름 조건으로 게시글 목록 조회 성공")
    void tag_name_condition_getPostPageResponse() {
        //given
        String tagName = tag.getName();
        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.tag,
                null,
                tagName,
                postRepository.findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(testMember1.getId(), tagName,PageRequest.of(0,PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(testMember1.getId(), null, tagName, null);

        //then
        postPageResponseTest(postPageResponse, find);
    }

    @Test
    @DisplayName("태그 이름과 페이지 번호 조건으로 게시글 목록 조회 성공")
    void tag_name_and_page_number_condition_getPostPageResponse() {
        //given
        String tagName = tag.getName();
        int pageNumber = 1;
        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.tag,
                null,
                tagName,
                postRepository.findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(testMember1.getId(), tagName,PageRequest.of(pageNumber-1, PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(testMember1.getId(), null, tagName, pageNumber);

        //then
        postPageResponseTest(postPageResponse, find);
    }

    @Test
    @DisplayName("제목 검색 조건으로 게시글 목록 조회 성공")
    void q_condition_getPostPageResponse() {
        //given
        String q = "제";
        String tagName = tag.getName();
        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.q,
                q,
                tagName,
                postRepository.findByAuthorIdOrderByCreatedDateDesc(testMember1.getId(), PageRequest.of(0, PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(testMember1.getId(), q, tagName, null);

        //then
        postPageResponseTest(postPageResponse, find);
    }

    @Test
    @DisplayName("제목 검색, 페이지 번호 조건으로 게시글 목록 조회 성공")
    void q_and_page_number_condition_getPostPageResponse() {
        //given
        String q = "제";
        String tagName = tag.getName();
        int p = 1;
        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.q,
                q,
                tagName,
                postRepository.findByAuthorIdOrderByCreatedDateDesc(testMember1.getId(), PageRequest.of(p-1, PAGE_SIZE)));

        //when
        PostPageResponse find = postService.getPostPageResponse(testMember1.getId(), q, tagName, p);

        //then
        postPageResponseTest(postPageResponse, find);
    }

    private void postPageResponseTest(PostPageResponse ppr, PostPageResponse findPpr) {
        assertThat(findPpr.getStartPage()).isEqualTo(ppr.getStartPage());
        assertThat(findPpr.getEndPage()).isEqualTo(ppr.getEndPage());
        assertThat(findPpr.getPageSize()).isEqualTo(ppr.getPageSize());
        assertThat(findPpr.getCurrentPage()).isEqualTo(ppr.getCurrentPage());
        assertThat(findPpr.getTotalPages()).isEqualTo(ppr.getTotalPages());
        assertThat(findPpr.getSearchCondition()).isEqualTo(ppr.getSearchCondition());
        assertThat(findPpr.getQ()).isEqualTo(ppr.getQ());
        assertThat(findPpr.getPosts().size()).isEqualTo(ppr.getPosts().size());
        for (int i=0; i<findPpr.getPosts().size(); i++) {
            assertThat(findPpr.getPosts().get(i)).isEqualTo(ppr.getPosts().get(i));
        }
        for (int i = 0; i < ppr.getPageNumbers().size(); i++) {
            assertThat(findPpr.getPageNumbers().get(i)).isEqualTo(ppr.getPageNumbers().get(i));
        }
    }


    @Test
    @DisplayName("비로그인 사용자가 게시글을 조회 성공")
    void non_login_getPostReadForm() {
        //given
        Member member = Member.create(null, null, null);
        memberRepository.save(member);

        Post post = Post.builder().author(member)
                .title("제목1")
                .customUrl(CustomUrl.create("1"))
                .build();
        postRepository.save(post);

        Tag tag = new Tag("testTag1");
        tagRepository.save(tag);

        PostWithTag postWithTag = new PostWithTag(post, tag);
        postWithTagRepository.save(postWithTag);

        PostResponse givenPostResponse = PostResponse.create(PostDto.create(post));
        CommentResponse givenCommentResponse = CommentResponse.create(new ArrayList<>());
        LikesResponse givenLikesResponse = LikesResponse.create(0, false, false);

        //when
        PostReadResponse postReadResponse = postService.getPostReadResponse(member.getId(), post.getStringUrl(), null);

        //then
        assertThat(postReadResponse.getPostResponse().getId()).isEqualTo(givenPostResponse.getId());
        assertThat(postReadResponse.getPrevPostResponse()).isEqualTo(null);
        assertThat(postReadResponse.getNextPostResponse()).isEqualTo(null);

    }

    @Test
    @DisplayName("게시글 작성 성공")
    void create_post() {
        //given
        PostWriteForm postWriteForm = PostWriteForm.builder().content("this is content")
                .preview("this is preview")
                .title("this is title")
                .url("testUrl")
                .tags("this is tag")
                .build();

        Member tester1 = Member.create(null, "Tester1", MemberRole.MEMBER);
        memberRepository.save(tester1);

        LoginMember loginMember = LoginMember.createMember(tester1);

        //when
        postService.createPost(postWriteForm, loginMember);

        //then
        Post createdPost = postRepository.findByAuthorIdAndCustomUrlUrl(loginMember.getId(), postWriteForm.getUrl())
                .orElseThrow(() -> new RuntimeException("게시글 생성 또는 게시글의 url 생성에 오류가 있습니다."));
        assertThat(createdPost.getTitle()).isEqualTo(postWriteForm.getTitle());
    }

    @Test
    @DisplayName("게시글 작성자가 게시글을 삭제")
    void delete_post_by_author() {
        //given
        Member member = Member.create(null, "테스터1", null);
        memberRepository.save(member);

        Post post = Post.builder().author(member).build();
        postRepository.save(post);

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
        postService.delete(loginMember, post.getId());

        //then
        assertThat(postRepository.findById(post.getId())).isEqualTo(Optional.empty());
        //PostWithTag pwt = postWithTagRepository.findById(postWithTag.getId()).get();
        //System.out.println("nickname = " + pwt.getPost().getAuthor().getNickname());
        assertThat(postWithTagRepository.findById(postWithTag.getId())).isEqualTo(Optional.empty());
        assertThat(commentRepository.findById(comment.getId())).isEqualTo(Optional.empty());
        assertThat(likesRepository.findById(likes.getId())).isEqualTo(Optional.empty());
    }

}