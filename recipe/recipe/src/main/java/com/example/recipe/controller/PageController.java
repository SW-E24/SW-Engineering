package com.example.recipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages")
public class PageController {
    // 회원가입 페이지로 이동 (index -> register)
    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    // 로그인 페이지로 이동 (index -> login)
    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    // 마이페이지로 이동
    @GetMapping("/mypage")
    public String myPage(Model model) {
        return "mypage";
    }
}
