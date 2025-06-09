package com.example.clonestagram.Controller;

import com.example.clonestagram.Dto.Response.PostResponseDto;
import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import com.example.clonestagram.Service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileService fileService;


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
    public String write(@RequestParam String content, Authentication auth, @RequestParam("file") MultipartFile file)
            throws IOException {


        if (auth == null) {
            return "/login";
        }

        // 로그인 상태일 시 메인 페이지로 전송할 유저 데이터
        MyUserDetailsService.CustomUser user =  (MyUserDetailsService.CustomUser) auth.getPrincipal();

        Optional<User> postUser = userRepository.findByUserId(user.userId);


        Post post = new Post();

        post.setContent(content);
        post.setUser(postUser.get());

        // 이미지 경로 저장
        String imagePath = fileService.uploadToStaticImg(file);
        if (imagePath != null) {
            post.setPostImg(imagePath);
        }

        postRepository.save(post);

        return "redirect:/main";


    }


    //글 수정 화면 Mapping
    @GetMapping("/edit/{postId}")
    public String editPostForm(@PathVariable Long postId, Authentication auth, Model model) {
        if (auth == null) {
            return "login";
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return "main";
        }

        Post post = postOpt.get();
        model.addAttribute("post", post);
        return "edit";




    }


    @PostMapping("/edit/{postId}")
    public String editPost(@PathVariable Long postId,
                           @RequestParam String content,
                           Authentication auth,
                           Model model) {
        if (auth == null) {
            return "login";
        }

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();

            MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
            model.addAttribute("loginUserId", user.userId);


            if (!post.getUser().getUserId().equals(user.userId)) {
                System.out.println("누구새요?");
                return "redirect:/main";
            }

            post.setContent(content);
            postRepository.save(post);
        }


        return "redirect:/main";
    }

    @GetMapping("/delete/{postId}")
    public String delete(@PathVariable Long postId ,Authentication auth, Model model) {

        if (auth == null) {
            return "login";
        }

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
        Optional<Post> postOpt = postRepository.findById(postId);

        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            if (post.getUser().getUserId().equals(user.userId)) {
                postRepository.delete(post);
            }
        }
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
