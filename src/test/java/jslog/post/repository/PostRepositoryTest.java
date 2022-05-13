package jslog.post.repository;

import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.postWithTag.domain.PostWithTag;
import jslog.postWithTag.repository.PostWithTagRepository;
import jslog.tag.domain.Tag;
import jslog.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired TagRepository tagRepository;
    @Autowired PostWithTagRepository postWithTagRepository;

    private final Member testMember1 = Member.create(null, "tester1", MemberRole.MEMBER);
    private final Member testMember2 = Member.create(null, "tester2", MemberRole.MEMBER);

    @BeforeEach
    void init() {
        memberRepository.save(testMember1);
        memberRepository.save(testMember2);
    }

    @Test
    @DisplayName("게시글 작가 ID와 url로 게시글 조회")
    void findByAuthorIdAndUrl() {
        //given
        Post post = Post.builder().author(testMember1)
                .customUrl(CustomUrl.create("testUrl"))
                .title("this is test title")
                .build();
        postRepository.save(post);

        //when
        Post findPost = postRepository.findByAuthorIdAndCustomUrlUrl(testMember1.getId(), post.getStringUrl())
                .orElseThrow(NoSuchElementException::new);

        //then
        assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(findPost.getAuthor()).isEqualTo(testMember1);
    }

    @Test
    @DisplayName("특정 작가의 게시글을 최신순으로 정렬하여 페이징")
    void findByAuthorIdOrderByCreatedDateDescTest() {
        //given
        Post post1 = Post.builder().author(testMember1).customUrl(CustomUrl.create("1")).build();
        Post post2 = Post.builder().author(testMember1).customUrl(CustomUrl.create("2")).build();
        Post post3 = Post.builder().author(testMember1).customUrl(CustomUrl.create("3")).build();
        Post post4 = Post.builder().author(testMember2).customUrl(CustomUrl.create("4")).build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

        //when
        Page<Post> page = postRepository.findByAuthorIdOrderByCreatedDateDesc(testMember1.getId(),
                PageRequest.of(0, 5));

        //then
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(1);

        sliceAuthorIdTest(page, testMember1.getId());
    }

    @Test
    @DisplayName("특정 작가의 게시글을 최신순으로 정렬하여 슬라이싱")
    void findSliceByAuthorIdOrderByCreatedDateDescTest() {
        //given
        Post post1 = Post.builder().author(testMember1).customUrl(CustomUrl.create("1")).build();
        Post post2 = Post.builder().author(testMember1).customUrl(CustomUrl.create("2")).build();
        Post post3 = Post.builder().author(testMember1).customUrl(CustomUrl.create("3")).build();
        Post post4 = Post.builder().author(testMember2).customUrl(CustomUrl.create("4")).build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

        //when
        Slice<Post> slice = postRepository.findSliceByAuthorIdOrderByCreatedDateDesc(testMember1.getId(),
                PageRequest.of(0, 5));

        //then
        sliceAuthorIdTest(slice, testMember1.getId());
    }

    @Test
    @DisplayName("게시글 제목으로 조회한 결과를 최신순으로 정렬하여 페이징")
    void findByTitleContainingOrderByCreatedDateDescTest() {
        //given
        Post post1 = Post.builder().author(testMember1).title("테스트페이지").build();
        Post post2 = Post.builder().author(testMember1).title("테스트 페이지").build();
        Post post3 = Post.builder().author(testMember1).title("테스 트페이지").build();
        Post post4 = Post.builder().author(testMember1).title("테 스트페 이지").build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

        //when
        Page<Post> page = postRepository.findByTitleContainingOrderByCreatedDateDesc("트페", PageRequest.of(0, 5));

        //then
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(3);

        for (Post post : page)
            assertThat(post.getTitle().indexOf("트페")).isGreaterThan(-1);
    }

    @Test
    @DisplayName("작가 ID와 게시글 제목으로 조회한 결과를 최신순으로 정렬하여 페이징")
    void findByAuthorIdAndTitleContainingOrderByCreatedDateDescTest() {
        //given
        Post post1 = Post.builder().author(testMember1).title("테스트페이지").build();
        Post post2 = Post.builder().author(testMember1).title("테스트 페이지").build();
        Post post3 = Post.builder().author(testMember1).title("테스 트페이지").build();
        Post post4 = Post.builder().author(testMember2).title("테 스트페 이지").build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

        //when
        Page<Post> page = postRepository.findByAuthorIdAndTitleContainingOrderByCreatedDateDesc(testMember1.getId(),
                "트페",
                PageRequest.of(0, 5));

        //then
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(2);

        sliceAuthorIdTest(page, testMember1.getId());
    }

    @Test
    @DisplayName("태그 이름과 작가 아이디로 조회한 결과를 최신순으로 정렬하여 페이징")
    void searchByTagAndAuthorIdOrderByCreatedDateDescTest() {
        //given
        Post post1 = Post.builder().author(testMember1).customUrl(CustomUrl.create("1")).build();
        Post post2 = Post.builder().author(testMember1).customUrl(CustomUrl.create("2")).build();
        Post post3 = Post.builder().author(testMember1).customUrl(CustomUrl.create("3")).build();
        Post post4 = Post.builder().author(testMember2).customUrl(CustomUrl.create("4")).build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

        String tagName1 = "testTag1";
        String tagName2 = "testTag2";
        Tag tag1 = new Tag(tagName1);
        Tag tag2 = new Tag(tagName2);
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        PostWithTag postWithTagWithTag1 = new PostWithTag(post1, tag1);
        PostWithTag postWithTagWithTag2 = new PostWithTag(post2, tag2);
        PostWithTag postWithTagWithTag3 = new PostWithTag(post3, tag2);
        PostWithTag postWithTagWithTag4 = new PostWithTag(post4, tag2);
        postWithTagRepository.save(postWithTagWithTag1);
        postWithTagRepository.save(postWithTagWithTag2);
        postWithTagRepository.save(postWithTagWithTag3);
        postWithTagRepository.save(postWithTagWithTag4);

        //when
        Page<Post> page = postRepository
                .findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(testMember1.getId(), tagName2, PageRequest.of(0, 3));

        //then
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    private void sliceAuthorIdTest(Slice<Post> slice, Long authorId) {
        for (Post post : slice)
            assertThat(post.getAuthor().getId()).isEqualTo(authorId);
    }


}