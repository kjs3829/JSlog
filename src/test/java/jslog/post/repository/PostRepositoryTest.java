package jslog.post.repository;

import jslog.member.member.domain.Member;
import jslog.member.member.repository.MemberRepository;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.postWithTag.domain.PostWithTag;
import jslog.postWithTag.repository.PostWithTagRepository;
import jslog.tag.domain.Tag;
import jslog.tag.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired TagRepository tagRepository;
    @Autowired PostWithTagRepository postWithTagRepository;

    @Test
    @DisplayName("게시글 작가 ID와 게시글의 url로 게시글 조회")
    void findByAuthorIdAndCustomUrlUrl() {
        //given
        Member testMember = Member.create(null, null, null);
        memberRepository.save(testMember);

        Post post = Post.builder().author(testMember)
                .customUrl(CustomUrl.create("testUrl"))
                .build();
        postRepository.save(post);

        //when
        Post findPost = postRepository.findByAuthorIdAndCustomUrlUrl(testMember.getId(), post.getStringUrl())
                .orElseThrow(NoSuchElementException::new);

        //then
        assertThat(findPost).isEqualTo(post);
    }

    @Test
    @DisplayName("특정 작가의 게시글을 최신순으로 정렬하여 페이징한 결과를 조회")
    void findByAuthorIdOrderByCreatedDateDescTest() {
        //given
        Member testMember = Member.create(null, null, null);
        memberRepository.save(testMember);

        Post post1 = Post.builder().author(testMember).build();
        Post post2 = Post.builder().author(testMember).build();
        Post post3 = Post.builder().author(testMember).build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        List<Post> posts = new ArrayList<>(Arrays.asList(post3,post2,post1));

        //when
        Page<Post> page = postRepository.findByAuthorIdOrderByCreatedDateDesc(testMember.getId(),
                PageRequest.of(0, 5));

        //then
        assertThat(page).contains(post3,post2,post1);
    }

    @Test
    @DisplayName("특정 작가의 게시글을 최신순으로 정렬하여 슬라이싱")
    void findSliceByAuthorIdOrderByCreatedDateDescTest() {
        //given
        Member testMember = Member.create(null, null, null);
        memberRepository.save(testMember);

        Post post1 = Post.builder().author(testMember).build();
        Post post2 = Post.builder().author(testMember).build();
        Post post3 = Post.builder().author(testMember).build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        List<Post> posts = new ArrayList<>(Arrays.asList(post3,post2,post1));

        //when
        Slice<Post> slice = postRepository.findSliceByAuthorIdOrderByCreatedDateDesc(testMember.getId(),
                PageRequest.of(0, 5));

        //then
        assertThat(slice).contains(post3,post2,post1);
    }

    @Test
    @DisplayName("게시글 제목으로 조회한 결과를 최신순으로 정렬하여 페이징")
    void findByTitleContainingOrderByCreatedDateDescTest() {
        //given
        Post post1 = Post.builder().title("테스트페이지").build();
        Post post2 = Post.builder().title("테스트 페이지").build();
        Post post3 = Post.builder().title("테스 트페이지").build();
        Post post4 = Post.builder().title("테 스트페 이지").build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        List<Post> posts = new ArrayList<>(Arrays.asList(post4,post3,post1));

        //when
        Page<Post> page = postRepository.findByTitleContainingOrderByCreatedDateDesc("트페", PageRequest.of(0, 5));

        //then
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page).contains(post4,post3,post1);
    }

    @Test
    @DisplayName("작가 ID와 게시글 제목으로 조회한 결과를 최신순으로 정렬하여 페이징")
    void findByAuthorIdAndTitleContainingOrderByCreatedDateDescTest() {
        //given
        Member testMember = Member.create(null, null, null);
        memberRepository.save(testMember);

        Post post1 = Post.builder().author(testMember).title("테스트페이지").build();
        Post post2 = Post.builder().author(testMember).title("테스트 페이지").build();
        Post post3 = Post.builder().author(testMember).title("테스 트페이지").build();
        Post post4 = Post.builder().author(testMember).title("테 스트페 이지").build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        List<Post> posts = new ArrayList<>(Arrays.asList(post4,post3,post1));

        //when
        Page<Post> page = postRepository.findByAuthorIdAndTitleContainingOrderByCreatedDateDesc(testMember.getId(),
                "트페",
                PageRequest.of(0, 5));

        //then
        assertThat(page).contains(post4,post3,post1);
    }

    @Test
    @DisplayName("태그 이름과 작가 아이디로 조회한 결과를 최신순으로 정렬하여 페이징")
    void searchByTagAndAuthorIdOrderByCreatedDateDescTest() {
        //given
        Member testMember = Member.create(null, null, null);
        memberRepository.save(testMember);

        Post post1 = Post.builder().author(testMember).build();
        Post post2 = Post.builder().author(testMember).build();
        postRepository.save(post1);
        postRepository.save(post2);

        Tag tag1 = new Tag("testTag1");
        tagRepository.save(tag1);

        PostWithTag postWithTagWithTag1 = new PostWithTag(post1, tag1);
        PostWithTag postWithTagWithTag2 = new PostWithTag(post2, tag1);
        postWithTagRepository.save(postWithTagWithTag1);
        postWithTagRepository.save(postWithTagWithTag2);

        List<Post> posts = new ArrayList<>(Arrays.asList(post2,post1));
        //when
        Page<Post> page = postRepository
                .findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(testMember.getId(), "testTag1", PageRequest.of(0, 5));

        //then
        assertThat(page).contains(post2,post1);
    }

}