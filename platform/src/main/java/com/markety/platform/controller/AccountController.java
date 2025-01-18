package com.markety.platform.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Value("${kakao.map-key}")
    private String kakaoMapKey;


    @GetMapping("/register")
    public String showRegister(Model model) {
        System.out.println(kakaoMapKey);
        model.addAttribute("mapKey", kakaoMapKey);
        return "account/register";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "account/login";
    }

}
