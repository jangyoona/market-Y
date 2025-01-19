package com.markety.platform.mapper;

import com.markety.platform.dto.RoleDto;
import com.markety.platform.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface UserMapper {

    List<UserDto> findAll();

    Optional<UserDto> findByUserId(long userId);

    Optional<UserDto>findByUserName(String userName);

    int insertUser(UserDto user);

    int updateUser(UserDto user);

    int deleteUser(long userId);

    /**
    * 유저 Role
    **/
    int insertUserRole(Map<String, Object> param);

    Set<RoleDto> findRolesById(long userId);
}
