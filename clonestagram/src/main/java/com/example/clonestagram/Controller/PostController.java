package com.example.clonestagram.Controller;

import com.example.clonestagram.Dto.Response.PostResponseDto;
import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    //글 작성 화면 Mapping
    @GetMapping("/write")
    public String write(Authentication auth, Model model) {

        if (auth == null) {
            return "/login";
        }

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        model.addAttribute("userId", user.userId);

        return "write";
    }

    @PostMapping("/write")
    public String write(@RequestParam String content, Authentication auth) {


        if (auth == null) {
            return "/login";
        }

        // 로그인 상태일 시 메인 페이지로 전송할 유저 데이터
        MyUserDetailsService.CustomUser user =  (MyUserDetailsService.CustomUser) auth.getPrincipal();

        Optional<User> postUser = userRepository.findByUserId(user.userId);


        Post post = new Post();

        post.setContent(content);
        post.setUser(postUser.get());

        postRepository.save(post);

        return "redirect:/main";
    }


    @GetMapping("/detail/{no}")
    @ResponseBody
    public PostResponseDto detail( @PathVariable Long no, Model model) {
        Optional<Post> post = postRepository.findById(no);

        PostResponseDto postResponseDto = new PostResponseDto();

        postResponseDto.setNo(post.get().getNo());
        postResponseDto.setUser(post.get().getUser());
        postResponseDto.setContent(post.get().getContent());
        postResponseDto.setPostRecommend(post.get().getPostRecommend());
        postResponseDto.setPostImg(post.get().getPostImg());
        postResponseDto.setPostDate(post.get().getPostDate());

        return postResponseDto;


        //return Map.of("content", post.get().getContent());
    }

}
