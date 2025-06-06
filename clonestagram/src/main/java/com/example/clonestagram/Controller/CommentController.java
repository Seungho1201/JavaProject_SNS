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

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @PostMapping("/post/comment")
    public ResponseEntity<?> postComment(@RequestBody CommentRequestDTO request, Authentication auth) {

        // 유저 로그인 여부
        MyUserDetailsService.CustomUser userData = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        // 비로그인시 에러 처리
        if(userData == null) {
            return ResponseEntity.status(401).build();
        }

        // 해당 게시글과 유저 찾기
        Optional<User> user = userRepository.findByUserId(userData.userId);
        Optional<Post> post = postRepository.findById(request.getPostId());

        // DB save
        Comment comment = new Comment();

        comment.setCommentContent(request.getCommentContent());
        comment.setUser(user.get());
        comment.setPost(post.get());

        commentRepository.save(comment);


        return ResponseEntity.ok().build();
    }

    //ResponseEntity<List<CommentResponseDTO>>
    @GetMapping("/get/commentlist/{id}")
    public ResponseEntity<?> getCommentList(@PathVariable Long id) {

        Optional<Post> posts = postRepository.findById(id);

        // 해당 게시글의 ID로 달린 댓글 리스트를 찾음
        List<Comment> comments = commentRepository.findByPost(posts.get());

        // 반환할 댓글DTO
        List<CommentResponseDTO> commentResponseDTOs = new ArrayList<>();

        // DTO 처리
        for(Comment comment : comments) {
            CommentResponseDTO commentResponseDTO = new CommentResponseDTO();

            commentResponseDTO.setNo(comment.getNo());
            commentResponseDTO.setUser(comment.getUser());
            commentResponseDTO.setCommentContent(comment.getCommentContent());

            commentResponseDTOs.add(commentResponseDTO);
        }

        return ResponseEntity.ok().body(commentResponseDTOs);
    }





}
