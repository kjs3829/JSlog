package jslog.likes.application;

import jslog.likes.domain.Likes;
import jslog.likes.repository.LikesRepository;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.repository.MemberRepository;
import jslog.post.repository.PostRepository;
import jslog.post.ui.dto.LikesResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class LikesService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;

    //TODO : 파라미터로 loginMemberId를 받게 수정해야함(그러기 위해서 비로그인 이용자에게 세션 발급해줘야함)
    public LikesResponse createLikesResponse(LoginMember loginMember, Long postId) {
        int likes = likesRepository.countLikesByPostId(postId);
        if (loginMember == null) {
            return LikesResponse.create(likes, false, false);
        } else {
            return LikesResponse.create(likes,true,likesRepository.findByMemberIdAndPostId(loginMember.getId(), postId).orElse(null)!=null);
        }
    }

    public Likes create(Long loginMemberId, Long postId) {
        return likesRepository.save(Likes.create(memberRepository.findById(loginMemberId).orElseThrow(NoSuchElementException::new),
                postRepository.findById(postId).orElseThrow(NoSuchElementException::new)));
    }

    public Long delete(Long loginMemberId, Long postId) {
        Likes deleteLikes = likesRepository.findByMemberIdAndPostId(loginMemberId, postId).orElseThrow(NoSuchElementException::new);
        likesRepository.delete(deleteLikes);

        return deleteLikes.getId();
    }

}
