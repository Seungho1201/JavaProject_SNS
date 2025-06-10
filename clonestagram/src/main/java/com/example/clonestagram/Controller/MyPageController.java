package com.example.clonestagram.Controller;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Entity.UserScrap;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Repository.UserScrapRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserScrapRepository userScrapRepository;

    // 마이페이지에서 내가 올린 게시물 조회
    @GetMapping("/posts")
    public String myPosts(Model model, Authentication auth) {

        // 로그인 안 돼 있으면 로그인 페이지로 리다이렉트
        if (auth == null) return "redirect:/login";

        // 로그인된 사용자 정보 가져오기
        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        Optional<User> userData = userRepository.findByUserId(user.userId);
        List<Post> userPost = postRepository.findByUser_UserId(user.userId);

        model.addAttribute("user", userData.get());


        model.addAttribute("userPost", userPost);      // 내가 쓴 게시글
        model.addAttribute("tab", "posts");     // 현재 탭 이름

        return "mypage";
    }

    // 마이페이지에서 태그된 게시물 조회(임시)
    @GetMapping("/tagged")
    public String taggedPosts(Model model, Authentication auth) {

        if (auth == null) return "redirect:/login";

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        Optional<User> userData = userRepository.findByUserId(user.userId);

        model.addAttribute("user", userData.get());
        model.addAttribute("tab", "tagged");

        return "mypage";
    }

    // 마이페이지에서 저장한 게시물 조회(임시)
    @GetMapping("/saved")
    public String savedPosts(Model model, Authentication auth) {

        if (auth == null) return "redirect:/login";

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();

        Optional<User> userData = userRepository.findByUserId(user.userId);

        List<UserScrap> userScraps = userScrapRepository.findByUser(userData.get());
        List<Post> userScrapPost = new ArrayList<>();

        for (UserScrap userScrap : userScraps) {
            //System.out.println(userScrap.toString());
            userScrapPost.add(userScrap.getPost());
        }

        model.addAttribute("userScraps", userScrapPost);
        model.addAttribute("user", userData.get());
        model.addAttribute("tab", "saved");

        return "mypage";
    }


    @PostMapping("/edit-profile")
    public String updateProfile(@RequestParam("profile") String profile,
                                Authentication auth,
                                @RequestParam(value = "profileImg", required = false) MultipartFile profileImg)
            throws IOException {
        // 로그인되지 않은 경우 로그인 페이지로 이동
        if (auth == null) return "redirect:/login";

        // 현재 로그인한 사용자 정보 가져오기
        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUserId(user.userId);

        if (optionalUser.isPresent()) {
            User u = optionalUser.get();

            // 소개글 업데이트
            u.setUserProfileMessage(profile);

            // 프로필 이미지 업로드 처리
            if (profileImg != null && !profileImg.isEmpty()) {
                // 외부 저장 경로: /uploads/profileImg/
                String uploadDir = System.getProperty("user.dir") + "/uploads/profileImg";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = UUID.randomUUID() + "_" + profileImg.getOriginalFilename();
                File target = new File(dir, fileName);
                profileImg.transferTo(target);

                // 저장 경로를 웹에서 접근 가능한 경로로 저장
                String webPath = "/uploads/profileImg/" + fileName;
                u.setUserProfile(webPath);
                user.userProfile = webPath; // 현재 세션에도 반영
            }

            // DB 저장 및 세션 갱신
            userRepository.save(u);
            user.userProfileMessage = profile;
        }
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
