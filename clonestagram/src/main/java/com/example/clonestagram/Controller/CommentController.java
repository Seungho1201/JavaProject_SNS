package com.example.clonestagram.Controller;

import com.example.clonestagram.Dto.Request.CommentRequestDTO;
import com.example.clonestagram.Dto.Response.CommentResponseDTO;
import com.example.clonestagram.Entity.Comment;
import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Repository.CommentRepository;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import com.example.clonestagram.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성 컨트롤러
    @PostMapping("/post/comment")
    public ResponseEntity<?> postComment(@RequestBody CommentRequestDTO request, Authentication auth) {

        return commentService.saveComment(request, auth);
    }

    // 댓글 목록 가져오는 컨트롤러
    @GetMapping("/get/commentlist/{id}")
    public ResponseEntity<?> getCommentList(@PathVariable Long id) {

        return commentService.getCommentList(id);
    }

}
