package com.example.clonestagram.Controller;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Entity.UserScrap;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Repository.UserRepository;
import com.example.clonestagram.Repository.UserScrapRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import com.example.clonestagram.Service.MyPageService;
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

    private final MyPageService myPageService;


    @GetMapping("/posts")
    public String myPosts(Model model, Authentication auth) {
        return myPageService.getMyPosts(model, auth);
    }

    @GetMapping("/tagged")
    public String taggedPosts(Model model, Authentication auth) {
        return myPageService.getTaggedPosts(model, auth);
    }

    @GetMapping("/saved")
    public String savedPosts(Model model, Authentication auth) {
        return myPageService.getSavedPosts(model, auth);
    }

    @PostMapping("/edit-profile")
    public String updateProfile(@RequestParam("profile") String profile,
                                Authentication auth,
                                @RequestParam(value = "profileImg", required = false) MultipartFile profileImg) throws IOException {
        return myPageService.updateProfile(profile, auth, profileImg);
    }

    @GetMapping("/edit-profile")
    public String editProfilePage(Model model, Authentication auth) {
        return myPageService.getEditProfilePage(model, auth);
    }
}
