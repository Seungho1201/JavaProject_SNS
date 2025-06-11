package com.example.clonestagram.Repository;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Entity.UserScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserScrapRepository extends JpaRepository<UserScrap, Long> {

    Optional<UserScrap> findByUserAndPost(User user, Post post);

    List<UserScrap> findByUser(User user);

    User post(Post post);
}
