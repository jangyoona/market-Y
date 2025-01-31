package com.markety.platform.controller;


import com.markety.platform.common.KaKaoApi;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
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
    public String showLogin(HttpServletRequest request, Model model) {
        String savedId = saveIdOnCookie(request);
        model.addAttribute("savedId", savedId);
        return "account/login";
    }


    private String saveIdOnCookie(HttpServletRequest request) {
        String savedId = "";

        // 쿠키에 저장된 아이디 찾아 반환
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("saveId")) {
                    savedId = cookie.getValue();
                }
            }
        }
        return savedId;
    }
}
