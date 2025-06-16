package com.example.clonestagram.Controller;

import com.example.clonestagram.Dto.Response.PostResponseDto;
import com.example.clonestagram.Entity.*;
import com.example.clonestagram.Repository.*;
import com.example.clonestagram.Security.MyUserDetailsService;
import com.example.clonestagram.Service.FileService;
import com.example.clonestagram.Service.UserScrapService;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final UserScrapService userScrapService;
    private final CommentRepository commentRepository;
    private final UserLikesRepository userLikesRepository;

    private final FileService fileService;
    private final UserScrapRepository userScrapRepository;


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
                           @RequestParam(value = "file", required = false) MultipartFile file,
                           Authentication auth,
                           Model model) throws IOException {
        if (auth == null) {
            return "login";
        }

        Optional<Post> postOpt = postRepository.findById(postId);

        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
            model.addAttribute("loginUserId", user.userId);

            if (!post.getUser().getUserId().equals(user.userId)) {
                return "redirect:/main";
            }

            post.setContent(content);

            // 이미지 수정이 있는 경우
            if (file != null && !file.isEmpty()) {
                String imagePath = fileService.uploadToStaticImg(file);
                if (imagePath != null) {
                    post.setPostImg(imagePath);
                }
            }

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

        Optional<User> postUser = userRepository.findByUserId(user.userId);
        Optional<Post> postOpt = postRepository.findById(postId);

        List<UserLikes> likesOpt = userLikesRepository.findByPost(postOpt.get());
        if (!likesOpt.isEmpty()) {
            userLikesRepository.deleteAll(likesOpt);
        }

        List<UserScrap> scrapOpt = userScrapRepository.findByPost(postOpt.get());
        if (!scrapOpt.isEmpty()) {
            userScrapRepository.deleteAll(scrapOpt);
        }


        List<Comment> comments = commentRepository.findByPost(postOpt.get());
        if (!comments.isEmpty()) {
            commentRepository.deleteAll(comments);
        }




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
