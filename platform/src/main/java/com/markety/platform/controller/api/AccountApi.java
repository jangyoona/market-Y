package com.markety.platform.controller.api;


import com.markety.platform.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountApi {

    private final AccountService accountService;

    @GetMapping("/check/{userName}")
    public ResponseEntity<Boolean> dupByUserName(@PathVariable String userName) {
        return ResponseEntity.ok(accountService.dupByUserName(userName));
    }


}
