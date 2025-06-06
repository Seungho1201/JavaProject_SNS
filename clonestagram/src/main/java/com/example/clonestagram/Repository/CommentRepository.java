package com.example.clonestagram.Repository;


import com.example.clonestagram.Entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
