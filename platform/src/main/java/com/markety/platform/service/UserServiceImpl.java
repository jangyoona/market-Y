package com.markety.platform.service;

import com.markety.platform.common.Util;
import com.markety.platform.dto.RoleDto;
import com.markety.platform.dto.UserDto;
import com.markety.platform.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        int result = userMapper.insertUser(user);

        // 이제 address, latitude, longitude 만 찾아서 저장시키면 끗

        Map<String, Object> param = new HashMap<>();
        param.put("roleId", 1);
        param.put("userId", user.getId());
        param.put("roleName", user.getType());
        userMapper.insertUserRole(param);

        return result;
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
