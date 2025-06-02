package com.example.clonestagram.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {

    // 메인 화면 Mapping
    @GetMapping("/main")
    public String main() {

        return "main";
    }

}
