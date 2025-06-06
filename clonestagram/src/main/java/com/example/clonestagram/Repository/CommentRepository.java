package com.example.clonestagram.Repository;


import com.example.clonestagram.Entity.Comment;
import com.example.clonestagram.Entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
