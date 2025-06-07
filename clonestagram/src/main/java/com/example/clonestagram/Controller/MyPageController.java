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

    // 프로필 수정(POST 요청)
    @PostMapping("/edit-profile")
    public String updateProfile(@RequestParam("profile") String profile, Authentication auth) {
        // 로그인되지 않은 경우 로그인 페이지로 리다이렉션
        if (auth == null) return "redirect:/login";

        // 현재 로그인한 사용자 정보 가져오기
        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        // DB에서 해당 사용자 엔티티 찾기
        Optional<User> optionalUser = userRepository.findByUserId(user.userId);

        // 존재할 경우 프로필 메시지를 업데이트하고 저장
        if (optionalUser.isPresent()) {
            User u = optionalUser.get();
            u.setUserProfileMessage(profile); // 소개(프로필 메시지) 업데이트
            userRepository.save(u);           // DB에 저장
        }

        // 수정 후 다시 edit-profile 페이지로 리다이렉션
        return "redirect:/mypage/edit-profile";
    }

    // 프로필 수정 페이지(GET 요청)
    @GetMapping("/edit-profile")
    public String editProfilePage(Model model, Authentication auth) {
        // 로그인되지 않은 경우 로그인 페이지로 리다이렉션
        if (auth == null) return "redirect:/login";

        // 현재 로그인한 사용자 정보 가져오기
        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        // 사용자 ID, 이름을 모델에 담기
        model.addAttribute("userId", user.userId);
        model.addAttribute("userName", user.getUsername());

        // DB에서 사용자 프로필 메시지 불러와서 모델에 추가
        Optional<User> optionalUser = userRepository.findByUserId(user.userId);
        optionalUser.ifPresent(value -> model.addAttribute("userProfileMessage", value.getUserProfileMessage()));

        // editprofile.html 페이지 렌더링
        return "editprofile";
    }
}
