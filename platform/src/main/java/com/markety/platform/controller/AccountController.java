package com.markety.platform.controller;


import com.markety.platform.common.KaKaoApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Value("${kakao.map-key}")
    private String kakaoMapKey;

    // .테스트용
    private final KaKaoApi kaKaoApi;

    public AccountController(KaKaoApi kaKaoApi) {
        this.kaKaoApi = kaKaoApi;
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("mapKey", kakaoMapKey);
        return "account/register";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "account/login";
    }
}
