package com.example.clonestagram.Dto.Response;

import com.example.clonestagram.Entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentResponseDTO {
    private Long no;
    private String commentContent;
    private User user;
}
