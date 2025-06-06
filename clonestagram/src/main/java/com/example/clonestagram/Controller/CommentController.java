package com.example.clonestagram.Controller;

import com.example.clonestagram.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;


}
