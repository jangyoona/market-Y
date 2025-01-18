package com.markety.platform.controller.api;


import com.markety.platform.common.SmsApi;
import com.markety.platform.service.AccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountApi {

    private final AuthenticationManager authenticationManager;

    private final AccountService accountService;
    private final SmsApi smsApi;

    @PostMapping("login")
    public ResponseEntity<HttpStatus> login(String userName, String password, boolean saveId, HttpServletRequest req, HttpServletResponse resp) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName,password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            saveUserNameOnCookie(req, resp, userName, saveId);
            return ResponseEntity.ok(HttpStatus.OK);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }

    @GetMapping("/check/{userName}")
    public ResponseEntity<Boolean> dupByUserName(@PathVariable String userName) {
        return ResponseEntity.ok(accountService.dupByUserName(userName));
    }

    @PostMapping("/check/phone")
    public ResponseEntity<HttpStatus> sendSmsCode(String phone, HttpSession session) {
        String code = smsApi.sendMessage(phone);
        session.setAttribute("smsCode", code);

        if (code != null) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/check/code")
    public ResponseEntity<HttpStatus> smsCodeValidate(String code, HttpSession session) {
        String sentCode = (String) session.getAttribute("smsCode");

        if (sentCode.equals(code)) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    private void saveUserNameOnCookie(HttpServletRequest request, HttpServletResponse response, String username, boolean saveId) {
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
            if (saveId) {
                Cookie newCookie = new Cookie("saveId", username);
                newCookie.setMaxAge(60 * 60 * 24 * 30); // 30일 저장
                newCookie.setPath("/");
                response.addCookie(newCookie);
            }
        }
    }


}
