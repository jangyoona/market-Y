package com.markety.platform.service;


import com.markety.platform.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;

    @Override
    public boolean dupByUserName(String userName) {
        return accountMapper.existsByUserName(userName);
    }


}
