package com.example.clonestagram.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue
    private int no;

    @Column(unique=true)
    private String userId;
    private String password;

    private String userName;
    private String userPhone;

    private String userProfile;
    private String userProfileMessage;

    @CreationTimestamp
    private String userRegisterDay;
}
