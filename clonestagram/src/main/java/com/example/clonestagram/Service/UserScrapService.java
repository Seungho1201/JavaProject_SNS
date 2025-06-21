package com.example.clonestagram.Service;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Entity.UserScrap;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Repository.UserScrapRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserScrapService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserScrapRepository userScrapRepository;

    public ResponseEntity<String> scrapPost(Long postId, Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        MyUserDetailsService.CustomUser userData = (MyUserDetailsService.CustomUser) auth.getPrincipal();
        Optional<User> user = userRepository.findByUserId(userData.userId);
        Optional<Post> post = postRepository.findById(postId);

        if (!user.isPresent() || !post.isPresent()) {
            return ResponseEntity.status(400).body("유효하지 않은 요청입니다.");
        }

        Optional<UserScrap> isScrap = userScrapRepository.findByUserAndPost(user.get(), post.get());

        if (isScrap.isPresent()) {

            userScrapRepository.delete(isScrap.get());

            return ResponseEntity.status(409).body("이미 스크랩한 게시글입니다.");
        }

        UserScrap userScrap = new UserScrap();
        userScrap.setUser(user.get());
        userScrap.setPost(post.get());

        userScrapRepository.save(userScrap);

        return ResponseEntity.status(201).body("스크랩 완료");
    }
}
