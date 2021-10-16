package com.jslog.blog.member;

import org.springframework.transaction.annotation.Transactional;

//@SpringBootTest
@Transactional
public class MemberMySQLRepositoryTest {
/*
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("findByEmail 성공")
    public void findByEmail_Success() {
        //given
        Member member1 = new Member.Builder().setEmail("test1@naver.com")
                .setPassword("test1").setNickname("테스터1")
                .setMemberRole(MemberRole.ADMIN).build();
        memberRepository.add(member1);

        //when
        Member findMember = memberRepository.findByEmail("test1@naver.com").orElse(null);

        //then
        Assertions.assertEquals(member1, findMember);
    }

    @Test
    @DisplayName("findById 성공")
    public void findById_Success() {
        //given
        Member member1 = new Member.Builder().setEmail("test1@naver.com")
                .setPassword("test1").setNickname("테스터1")
                .setMemberRole(MemberRole.ADMIN).build();
        memberRepository.add(member1);

        //when
        Long id = member1.getId();
        Member findMember = memberRepository.findById(id);

        //then
        Assertions.assertEquals(member1, findMember);
    }

    @Test
    @DisplayName("Delete 성공")
    public void delete_Success() {
        //given
        Member member1 = new Member.Builder().setEmail("test1@naver.com")
                .setPassword("test1").setNickname("테스터1")
                .setMemberRole(MemberRole.ADMIN).build();
        Member member2 = new Member.Builder().setEmail("test2@naver.com")
                .setPassword("test2").setNickname("테스터2")
                .setMemberRole(MemberRole.ADMIN).build();
        memberRepository.add(member1);
        memberRepository.add(member2);

        //when
        Long id1 = member1.getId();
        memberRepository.delete(id1);
        Member findMember = memberRepository.findById(id1);

        //then
        Assertions.assertNull(findMember);
    }

 */
}
