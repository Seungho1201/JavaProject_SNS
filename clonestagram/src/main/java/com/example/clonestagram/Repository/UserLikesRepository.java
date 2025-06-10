package com.example.clonestagram.Repository;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Entity.UserLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {
    Optional<UserLikes> findByUser(User user);

    Optional<UserLikes> findByPostAndUser (Post post, User user);
}
