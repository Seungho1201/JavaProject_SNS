package com.example.clonestagram.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue
    private Long no;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private String content;
    private String postRecommend;
    private String postImg;

    @CreationTimestamp
    private String postDate;

}
