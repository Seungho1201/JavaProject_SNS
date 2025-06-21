package com.example.clonestagram.Controller;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Entity.UserScrap;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Repository.UserScrapRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import com.example.clonestagram.Service.UserScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserScrapController {

    private final UserScrapService userScrapService;


    // 유저 게시글 스크랩 저장 및 삭제
    @PostMapping("/scrap/{postId}")
    public ResponseEntity<String> save(@PathVariable Long postId, Authentication auth) {
        return userScrapService.scrapPost(postId, auth);
    }


}
