package com.example.clonestagram.Controller;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Entity.UserLikes;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserLikesRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import com.example.clonestagram.Service.UserLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserLikesController {

    private final UserLikesRepository userLikesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final UserLikesService userLikesService;

    // 추천 증가 컨트롤러
    @PostMapping("/post/recommend/{id}")
    public ResponseEntity<String> recommend(@PathVariable Long id, Authentication auth) {

        // Service 레이어 분리
        return userLikesService.recommendPost(id, auth);
    }

    // 추천 취소 컨트롤러
    @PostMapping("/post/cancel/recommend/{id}")
    public ResponseEntity<String> cancelRecommend(@PathVariable Long id, Authentication auth) {

        // Service 레이어 분리
        return userLikesService.cancelRecommend(id, auth);
    }
}
