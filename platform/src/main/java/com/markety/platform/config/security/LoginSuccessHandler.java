package com.markety.platform.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 아이디 저장 설정
        String saveId = request.getParameter("saveId"); // true or false
        String username = authentication.getName();
        saveUserNameOnCookie(request, response, username, saveId);

        response.sendRedirect("/");

    }


    private void saveUserNameOnCookie(HttpServletRequest request, HttpServletResponse response, String username, String saveId) {
        if (username != null) {
            // 기존 쿠키 있으면 삭제
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("saveId")) {
                        // 기존 쿠키가 있으면 삭제
                        Cookie deleteCookie = new Cookie("saveId", "");
                        deleteCookie.setMaxAge(0);
                        deleteCookie.setPath("/");
                        response.addCookie(deleteCookie);
                        break;
                    }
                }
            }

            // 아이디 저장 체크하면 쿠키 새로 저장 or 미 체크시 새로 저장안하고 위에서 삭제만 됨.
            if (saveId != null) {
                Cookie newCookie = new Cookie("saveId", username);
                newCookie.setMaxAge(60 * 60 * 24 * 30); // 30일 저장
                newCookie.setPath("/");
                response.addCookie(newCookie);
            }
        }
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
    }
}
