package com.example.clonestagram.Dto.Response;

import com.example.clonestagram.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
public class PostResponseDto {
    private Long no;
    private User user;
    private String content;
    private int postRecommend;
    private String postImg;
    private String postDate;
}
