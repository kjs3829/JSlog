package jslog.tag.repository;

import jslog.member.member.repository.MemberRepository;
import jslog.post.repository.PostRepository;
import jslog.postWithTag.repository.PostWithTagRepository;
import jslog.tag.domain.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class tagRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private PostWithTagRepository postWithTagRepository;
    @Autowired private TagRepository tagRepository;

    @Test
    @DisplayName("태그명으로 DB에 존재하는 태그 조회 성공")
    void findTagByName() {
        //given
        String tagName1 = "tag1";
        Tag tag1 = new Tag(tagName1);
        tagRepository.save(tag1);

        //when
        Tag findTag1 = tagRepository.findTagByName(tagName1).get();

        //then
        assertThat(findTag1).isEqualTo(tag1);
    }

}
