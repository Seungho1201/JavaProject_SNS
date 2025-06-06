package com.example.clonestagram.Repository;

import com.example.clonestagram.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
