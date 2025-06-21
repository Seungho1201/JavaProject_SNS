package com.example.clonestagram.Controller;

import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //회원가입 페이지
    @GetMapping("/signup")
    String signupPage(){
        return "signup.html";
    }

    // 회원가입 기능
    @PostMapping("/signupUser")
    String signupUser(
            @RequestParam String userId,
            @RequestParam String userPassword,
            @RequestParam String userName,
            @RequestParam String userEmail){

        // Service 레이어
        User newUser = new User();
        newUser.setUserId(userId);
        newUser.setPassword(passwordEncoder.encode(userPassword));
        newUser.setUserName(userName);
        newUser.setUserPhone(userEmail);


        userRepository.save(newUser);


        // 회원가입 완료 후 로그인 페이지로
        return "main";

    }

    // 마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model, Authentication auth) {        // Authentication : Spring이 자동으로 넣어주는 현재 로그인 사용자 정보임

        // 로그인 안 된 경우 로그인 페이지로 리디렉션
        if (auth == null) return "login";

        // 로그인한 사용자 정보 가져오기
        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        // Entity 사용
        model.addAttribute("userId", user.userId);
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("userProfile", user.userProfile);

        // 게시글 정보
        // model.addAttribute("userPost", postRepository.findByPostUserId(user.userId));

        return "mypage";
    }

    // 정보 수정 페이지
    @GetMapping("/editprofile")
    public String editprofile(Model model, Authentication auth) {

        // 로그인 안 된 경우 로그인 페이지로 리디렉션
        if (auth == null) return "login";

        // 로그인한 사용자 정보 가져오기
        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        // Entity 사용
        model.addAttribute("userId", user.userId);
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("userProfileMessage", user.userProfileMessage);
        model.addAttribute("userProfileImg", user.userProfile);

        return "editprofile";
    }
}
