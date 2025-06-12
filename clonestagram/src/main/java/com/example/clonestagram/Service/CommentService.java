package com.example.clonestagram.Service;

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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> saveComment(CommentRequestDTO request, Authentication auth) {
        MyUserDetailsService.CustomUser userData = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        if(userData == null) {
            return ResponseEntity.status(401).build();
        }

        Optional<User> user = userRepository.findByUserId(userData.userId);
        Optional<Post> post = postRepository.findById(request.getPostId());

        Comment comment = new Comment();
        comment.setCommentContent(request.getCommentContent());
        comment.setUser(user.get());
        comment.setPost(post.get());

        commentRepository.save(comment);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getCommentList(Long postId) {
        Optional<Post> posts = postRepository.findById(postId);
        List<Comment> comments = commentRepository.findByPost(posts.get());

        List<CommentResponseDTO> commentResponseDTOs = new ArrayList<>();
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
