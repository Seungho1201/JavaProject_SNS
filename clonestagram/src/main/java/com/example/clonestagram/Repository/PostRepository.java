package com.example.clonestagram.Repository;

import com.example.clonestagram.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser_UserId(String userId);
}
