package com.markety.platform.controller.api;


import com.markety.platform.common.KaKaoApi;
import com.markety.platform.common.SmsApi;
import com.markety.platform.config.security.WebUserDetails;
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

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountApi {

    private final AuthenticationManager authenticationManager;

    private final AccountService accountService;
    private final SmsApi smsApi;
    private final KaKaoApi kaKaoApi;

    @PostMapping("user-location")
    public ResponseEntity<String> userLocation(double latitude, double longitude) {
        // 유저 현재 위치 반환
        Map<String, Object> result = kaKaoApi.getKakaoSearch(latitude, longitude);
        System.out.println(result.get("x"));
        System.out.println(result.get("y"));
        System.out.println(result.get("address_origin"));
        System.out.println(result.get("address"));

        return ResponseEntity.ok(result.get("address").toString());
    }

    @PostMapping("login")
    public ResponseEntity<HttpStatus> login(String userName, String password, boolean saveId,
                                            HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName,password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Security Context 에 사용자 정보가 정상적으로 저장이 안됨
            // 아래와 같이 명시적으로 해주니 해결되긴 했는데 궁금해서 찾아봄
            // SecurityContextPersistenceFilter 가 실행되는 순서의 문제인거 같음
            // configuration 에서 addFilterBefore() 를 통해 순서를 앞으로 당겨주니 해결
            // 참고 블로그 https://farmfarm1223.tistory.com/109
//            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
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
            Arrays.stream(cookies)
                    .filter(cookie -> "saveId".equals(cookie.getName()))  // "saveId" 쿠키 필터링
                    .findFirst()  // 첫 번째 쿠키만 처리
                    .ifPresent(cookie -> {
                        Cookie deleteCookie = new Cookie("saveId", null);
                        deleteCookie.setMaxAge(0);
                        deleteCookie.setPath("/");
                        response.addCookie(deleteCookie);  // 쿠키 삭제
                    });

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
