package jslog.post.application;

import jslog.member.auth.domain.Provider;
import jslog.member.auth.domain.ProviderName;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.PostPageViewer;
import jslog.post.SearchCondition;
import jslog.post.repository.PostRepository;
import jslog.post.ui.dto.PostWriteForm;
import jslog.postWithTag.domain.PostWithTag;
import jslog.postWithTag.repository.PostWithTagRepository;
import jslog.tag.domain.Tag;
import jslog.tag.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired PostService postService;
    @Autowired PostRepository postRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostWithTagRepository postWithTagRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("searchCondition이 none인 pageSelector ")
    void getPageSelector() {
        //given
        Member member = Member.create(Provider.create("1", ProviderName.LOCAL), "tester1", MemberRole.MEMBER);
        memberRepository.save(member);

        jslog.post.domain.Post post1 = jslog.post.domain.Post.builder().author(member)
                .url("1")
                .build();
        jslog.post.domain.Post post2 = jslog.post.domain.Post.builder().author(member)
                .url("2")
                .build();
        jslog.post.domain.Post post3 = jslog.post.domain.Post.builder().author(member)
                .url("3")
                .build();
        jslog.post.domain.Post post4 = jslog.post.domain.Post.builder().author(member)
                .url("4")
                .build();
        jslog.post.domain.Post post5 = jslog.post.domain.Post.builder().author(member)
                .url("5")
                .build();
        jslog.post.domain.Post post6 = jslog.post.domain.Post.builder().author(member)
                .url("6")
                .build();
        Tag tag = new Tag("testTag");
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);
        postRepository.save(post6);
        tagRepository.save(tag);
        PostWithTag postWithTagWithTag1 = new PostWithTag(post1, tag);
        postWithTagRepository.save(postWithTagWithTag1);
        PostWithTag postWithTagWithTag2 = new PostWithTag(post2, tag);
        postWithTagRepository.save(postWithTagWithTag2);
        PostWithTag postWithTagWithTag3 = new PostWithTag(post3, tag);
        postWithTagRepository.save(postWithTagWithTag3);
        PostWithTag postWithTagWithTag4 = new PostWithTag(post4, tag);
        postWithTagRepository.save(postWithTagWithTag4);
        PostWithTag postWithTagWithTag5 = new PostWithTag(post5, tag);
        postWithTagRepository.save(postWithTagWithTag5);
        PostWithTag postWithTagWithTag6 = new PostWithTag(post5, tag);
        postWithTagRepository.save(postWithTagWithTag6);

        //when
        PostPageViewer pageSelector = postService.getPageSelector(member.getId(), null, null, null);

        //then
        int startPage = 1;
        int endPage = 2;
        assertThat(pageSelector.getStartPage()).isEqualTo(startPage);
        assertThat(pageSelector.getEndPage()).isEqualTo(endPage);
        assertThat(pageSelector.getPageSize()).isEqualTo(5);
        assertThat(pageSelector.getCurrentPage()).isEqualTo(1);
        assertThat(pageSelector.getTotalPages()).isEqualTo(2);
        assertThat(pageSelector.getSearchCondition()).isEqualTo(SearchCondition.none);
        assertThat(pageSelector.getQ()).isEqualTo(null);
        for (Integer pn : pageSelector.getPageNumbers()) {
            assertThat(pn).isEqualTo(startPage++);
        }
        assertThat(startPage).isEqualTo(endPage+1);
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
        boolean create = postService.createPost(postWriteForm, loginMember);

        //then
        jslog.post.domain.Post createdPost = postRepository.findByAuthorIdAndUrl(loginMember.getId(), postWriteForm.getUrl())
                .orElseThrow(() -> new RuntimeException("게시글 생성 또는 게시글의 url 생성에 오류가 있습니다."));
        assertThat(create).isTrue();
        assertThat(createdPost.getTitle()).isEqualTo(postWriteForm.getTitle());
    }

}