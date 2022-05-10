package jslog.tag.repository;

import jslog.member.auth.domain.Provider;
import jslog.member.auth.domain.ProviderName;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.domain.Post;
import jslog.post.repository.PostRepository;
import jslog.postWithTag.domain.PostWithTag;
import jslog.postWithTag.repository.PostWithTagRepository;
import jslog.tag.MemberTag;
import jslog.tag.domain.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class tagRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private PostWithTagRepository postWithTagRepository;
    @Autowired private TagRepository tagRepository;

    private final Member member = Member.create(Provider.create("1", ProviderName.LOCAL), "tester1", MemberRole.MEMBER);
    private final String tagName1 = "tag1";
    private final String tagName2 = "tag2";

    @Test
    @DisplayName("TagName으로 Tag 조회")
    void findTagByNameTest() {
        //given
        Tag tag1 = new Tag(tagName1);
        Tag tag2 = new Tag(tagName2);
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        //when
        Tag findTag1 = tagRepository.findTagByName(tagName1).get();
        Tag findTag2 = tagRepository.findTagByName(tagName2).get();
        Tag findTag3 = tagRepository.findTagByName("tagName3").orElse(null);

        //then
        assertThat(findTag1).isEqualTo(tag1);
        assertThat(findTag2).isEqualTo(tag2);
        assertThat(findTag3).isEqualTo(null);
    }

    @Test
    @DisplayName("작가 ID로 MemberTags(=태그 목록과 태그별 작성된 게시글 수) 조회")
    void getMemberTagsTest() {
        //given
        memberRepository.save(member);

        Post post1 = Post.builder().author(member).url("1").build();
        Post post2 = Post.builder().author(member).url("2").build();
        Post post3 = Post.builder().author(member).url("3").build();
        Post post4 = Post.builder().author(member).url("4").build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

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
        final int tag1Count = 1;
        final int tag2Count = 3;

        //when
        List<MemberTag> memberTags = tagRepository.getMemberTags(member.getId());

        //then
        assertThat(memberTags.size()).isEqualTo(2);
        for (MemberTag memberTag : memberTags) {
            if (memberTag.getTagName().equals(tagName1)) assertThat(memberTag.getTagCount()).isEqualTo(tag1Count);
            if (memberTag.getTagName().equals(tagName2)) assertThat(memberTag.getTagCount()).isEqualTo(tag2Count);
            assertThat(memberTag.getAuthorId()).isEqualTo(member.getId());
        }
    }


}
