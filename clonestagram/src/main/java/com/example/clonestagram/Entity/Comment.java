package com.example.clonestagram.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue
    private Long no;                // 댓글 번호

    private Long commentContent;    // 댓글 내용

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;              // 어느 유저가 작성한 것인지

    @ManyToOne
    @JoinColumn(name = "post")
    private Post post;              // 어느 포스트에 저장된 아이디인지
}
