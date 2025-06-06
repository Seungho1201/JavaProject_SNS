package com.example.clonestagram.Controller;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class mainController {

    @Autowired
    private PostRepository postRepository;


    @PostMapping("/write")
    public String write(@RequestParam String content) {
        Post post = new Post();
        post.setContent(content);
        postRepository.save(post);
        return "redirect:/main";
    }

    // 메인 화면 Mapping
    @GetMapping("/main")
    public String main(Model model) {
        List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "no"));
        model.addAttribute("posts", postList);
        return "main";
    }

    //글 작성 화면 Mapping
    @GetMapping("/write")
    public String write() {

        return "write";
    }
}
