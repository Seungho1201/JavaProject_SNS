package com.example.clonestagram.Controller;

import com.example.clonestagram.Entity.Post;
import com.example.clonestagram.Entity.User;
import com.example.clonestagram.Repository.PostRepository;
import com.example.clonestagram.Security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class mainController {


    private final PostRepository postRepository;


    // 메인 화면 Mapping
    @GetMapping("/main")
    public String main(Authentication auth ,Model model) {
        if(auth == null){
            return "login";
        }

        if(auth != null){
            MyUserDetailsService.CustomUser user1 = (MyUserDetailsService.CustomUser) auth.getPrincipal();

            System.out.println(user1.toString());
            System.out.println(user1.userProfile);
            model.addAttribute("user", user1);
        }

        //다른 유저의 글에는 수정 삭제 버튼 뜨지 않게 하기 위함
        if (auth != null && auth.isAuthenticated()) {
            MyUserDetailsService.CustomUser user = (MyUserDetailsService.CustomUser) auth.getPrincipal();
            model.addAttribute("loginUserId", user.userId);
        }

        List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "no"));

        model.addAttribute("posts", postList);

        return "main";
    }


}
