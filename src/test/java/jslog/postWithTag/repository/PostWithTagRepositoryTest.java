package jslog.postWithTag.repository;

import jslog.member.auth.domain.Provider;
import jslog.member.auth.domain.ProviderName;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.domain.Post;
import jslog.post.repository.PostRepository;
import jslog.postWithTag.domain.PostWithTag;
import jslog.post.ui.dto.MemberTag;
import jslog.tag.domain.Tag;
import jslog.tag.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostWithTagRepositoryTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private PostWithTagRepository postWithTagRepository;
    @Autowired private TagRepository tagRepository;

    @Test
    @DisplayName("작가 ID로 MemberTags(=태그 목록과 태그별 작성된 게시글 수) 조회")
    void getMemberTagsTest() {
        //given
        Member member = Member.create(Provider.create("1", ProviderName.LOCAL), "tester1", MemberRole.MEMBER);
        memberRepository.save(member);

        Post post1 = Post.builder().author(member).build();
        postRepository.save(post1);

        Tag tag1 = new Tag("tag1");
        tagRepository.save(tag1);
        Tag tag2 = new Tag("tag2");
        tagRepository.save(tag2);

        PostWithTag postWithTagWithTag1 = new PostWithTag(post1, tag1);
        PostWithTag postWithTagWithTag2 = new PostWithTag(post1, tag2);
        postWithTagRepository.save(postWithTagWithTag1);
        postWithTagRepository.save(postWithTagWithTag2);

        //when
        List<MemberTag> memberTags = postWithTagRepository.getMemberTags(member.getId());

        //then
        assertThat(memberTags.size()).isEqualTo(2);
        for (MemberTag memberTag : memberTags) {
            assertThat(memberTag.getAuthorId()).isEqualTo(member.getId());
        }
    }

}