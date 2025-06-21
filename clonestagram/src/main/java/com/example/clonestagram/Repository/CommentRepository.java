package com.example.clonestagram.Repository;


import com.example.clonestagram.Entity.Comment;
import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByPostAndUser(Post post, User user);
    //Optional<Comment> findByPostAndUser(Post post, User user);
}
