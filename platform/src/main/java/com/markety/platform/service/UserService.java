package com.markety.platform.service;

import com.markety.platform.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();

    Optional<UserDto> getUserById(long userId);

    @Transactional
    int createUser(UserDto user);

    int updateUser(long userId, UserDto user);

    int deleteUser(long userId);

}
