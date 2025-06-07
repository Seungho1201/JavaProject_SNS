package com.example.clonestagram.Controller;

import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    // 마이페이지에서 내가 올린 게시물 조회
    @GetMapping("/posts")
    public String myPosts(Model model, Authentication auth) {

        // 로그인 안 돼 있으면 로그인 페이지로 리다이렉트
        if (auth == null) return "redirect:/login";

        // 로그인된 사용자 정보 가져오기
        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        // 모델에 사용자 정보와 게시물 정보 담기
        model.addAttribute("userId", user.userId);
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("userPost", postRepository.findByUser_UserId(user.userId));      // 내가 쓴 게시글
        model.addAttribute("tab", "posts");     // 현재 탭 이름

        return "mypage";
    }

    // 마이페이지에서 태그된 게시물 조회(임시)
    @GetMapping("/tagged")
    public String taggedPosts(Model model, Authentication auth) {
        if (auth == null) return "redirect:/login";

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
        model.addAttribute("userId", user.userId);
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("userPost", postRepository.findByUser_UserId(user.userId));
        model.addAttribute("tab", "tagged");

        return "mypage";
    }

    // 마이페이지에서 저장한 게시물 조회(임시)
    @GetMapping("/saved")
    public String savedPosts(Model model, Authentication auth) {
        if (auth == null) return "redirect:/login";

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
        model.addAttribute("userId", user.userId);
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("userPost", postRepository.findByUser_UserId(user.userId));
        model.addAttribute("tab", "saved");

        return "mypage";
    }
}
