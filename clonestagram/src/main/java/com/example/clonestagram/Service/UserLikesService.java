package com.example.clonestagram.Service;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Entity.UserLikes;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserLikesRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLikesService {

    private final UserLikesRepository userLikesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    // 추천수 증가 및 추천 리스트 저장 서비스
    public ResponseEntity<String> recommendPost(Long postId, Authentication auth) {
        if (auth == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");

        // 유저 로그인 세션 데이터 가져오기
        MyUserDetailsService.CustomUser userData = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        // 데이터 find
        Optional<User> user = userRepository.findByUserId(userData.userId);
        Optional<Post> post = postRepository.findById(postId);

        // 유저나 포스트 하나라도 존재안할시 return
        if (!user.isPresent() || !post.isPresent()) {
            return ResponseEntity.badRequest().body("잘못된 요청");
        }

        Optional<UserLikes> isUserLikes = userLikesRepository.findByPostAndUser(post.get(), user.get());

        if (isUserLikes.isPresent()) {
            return ResponseEntity.badRequest().body("이미 추천 함");
        }

        // 좋아요 등록
        UserLikes userLikes = new UserLikes();
        userLikes.setUser(user.get());
        userLikes.setPost(post.get());

        userLikesRepository.save(userLikes);

        // 추천수 증가
        Post target = post.get();
        target.setPostRecommend(target.getPostRecommend() + 1);

        postRepository.save(target);

        return ResponseEntity.ok("추천 완료");
    }

    // 추천 취소 로직 서비스
    public ResponseEntity<String> cancelRecommend(Long postId, Authentication auth) {

        // 로그인 여부 확인
        if (auth == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        // 세션 데이터 가져오기
        MyUserDetailsService.CustomUser userData = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        // 포스트와 유저 찾기
        Optional<User> user = userRepository.findByUserId(userData.userId);
        Optional<Post> post = postRepository.findById(postId);

        // 존재 X 일시 에러처리
        if (user.isEmpty() || post.isEmpty()) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        // 이미 존재하는지 확인
        Optional<UserLikes> userLikes = userLikesRepository.findByPostAndUser(post.get(), user.get());

        // 이미 존재할시 없는 데이터 이므로 return
        if (userLikes.isEmpty()) {
            return ResponseEntity.badRequest().body("이미 추천이 취소된 상태입니다.");
        }

        // 해당 컬럼 삭제
        userLikesRepository.delete(userLikes.get());

        // 포스트이 추천 수로 감소
        Post target = post.get();
        target.setPostRecommend(Math.max(0, target.getPostRecommend() - 1));

        postRepository.save(target);

        return ResponseEntity.ok("추천 취소 완료");
    }
}
