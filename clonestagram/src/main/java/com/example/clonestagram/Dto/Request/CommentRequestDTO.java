package com.example.clonestagram.Dto.Request;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentRequestDTO {
    private String commentContent;
    private Long postId;
    private User user;
}
