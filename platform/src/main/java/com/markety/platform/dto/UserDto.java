package com.markety.platform.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class UserDto {

    private long id;
    private String userName;
    private String password;
    private String nickName;
    private String phone;
    private double latitude;
    private double longitude;
    private Timestamp createdAt;
    private String type = "ROLE_USER";
    private boolean active = true;

    private Set<RoleDto> roles;
}
