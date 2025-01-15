package com.markety.platform.mapper;

import com.markety.platform.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    List<UserDto> findAll();

    Optional<UserDto> findByUserId(long userId);

    int insertUser(UserDto user);

    int updateUser(UserDto user);

    int deleteUser(long userId);
}
