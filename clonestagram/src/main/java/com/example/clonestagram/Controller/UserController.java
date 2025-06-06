package com.example.clonestagram.Controller;

import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
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
        return "/main";

    }

}
