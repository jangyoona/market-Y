package com.markety.platform.service;

import com.markety.platform.common.Util;
import com.markety.platform.dto.UserDto;
import com.markety.platform.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.findAll();
    }

    @Override
    public Optional<UserDto> getUserById(long userId) {
        return userMapper.findByUserId(userId);
    }

    @Transactional
    @Override
    public int createUser(UserDto user) {
        String hashedPasswd = Util.getHashedString(user.getPassword(), "SHA-256");
        user.setPassword(hashedPasswd);
        return userMapper.insertUser(user);
    }

    @Override
    public int updateUser(long userId, UserDto user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int deleteUser(long userId) {
        return userMapper.deleteUser(userId);
    }


}
