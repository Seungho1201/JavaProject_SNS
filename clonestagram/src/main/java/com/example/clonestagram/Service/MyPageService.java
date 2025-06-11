package com.example.clonestagram.Service;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Entity.UserScrap;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Repository.UserScrapRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserScrapRepository userScrapRepository;


    public String getMyPosts(Model model, Authentication auth) {
        if (auth == null) return "redirect:/login";

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
        Optional<User> userData = userRepository.findByUserId(user.userId);
        List<Post> userPost = postRepository.findByUser_UserId(user.userId);

        model.addAttribute("user", userData.get());
        model.addAttribute("userPost", userPost);
        model.addAttribute("tab", "posts");

        return "mypage";
    }


    public String getTaggedPosts(Model model, Authentication auth) {
        if (auth == null) return "redirect:/login";

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
        Optional<User> userData = userRepository.findByUserId(user.userId);

        model.addAttribute("user", userData.get());
        model.addAttribute("tab", "tagged");

        return "mypage";
    }

    public String getSavedPosts(Model model, Authentication auth) {
        if (auth == null) return "redirect:/login";

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
        Optional<User> userData = userRepository.findByUserId(user.userId);

        List<UserScrap> userScraps = userScrapRepository.findByUser(userData.get());
        List<Post> userScrapPost = new ArrayList<>();

        for (UserScrap userScrap : userScraps) {
            userScrapPost.add(userScrap.getPost());
        }

        model.addAttribute("userScraps", userScrapPost);
        model.addAttribute("user", userData.get());
        model.addAttribute("tab", "saved");

        return "mypage";
    }

    public String updateProfile(String profile, Authentication auth, MultipartFile profileImg) throws IOException {
        if (auth == null) return "redirect:/login";

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUserId(user.userId);

        if (optionalUser.isPresent()) {
            User u = optionalUser.get();
            u.setUserProfileMessage(profile);

            if (profileImg != null && !profileImg.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/profileImg";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = UUID.randomUUID() + "_" + profileImg.getOriginalFilename();
                File target = new File(dir, fileName);
                profileImg.transferTo(target);

                String webPath = "/uploads/profileImg/" + fileName;
                u.setUserProfile(webPath);
                user.userProfile = webPath;
            }

            userRepository.save(u);
            user.userProfileMessage = profile;
        }

        return "redirect:/mypage/edit-profile";
    }

    public String getEditProfilePage(Model model, Authentication auth) {
        if (auth == null) return "redirect:/login";

        MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
        model.addAttribute("userId", user.userId);
        model.addAttribute("userName", user.getUsername());

        Optional<User> optionalUser = userRepository.findByUserId(user.userId);
        optionalUser.ifPresent(value -> model.addAttribute("userProfileMessage", value.getUserProfileMessage()));

        return "editprofile";
    }

}
