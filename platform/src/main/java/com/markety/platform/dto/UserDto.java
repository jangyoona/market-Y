package com.markety.platform.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDto {

    private long id;
    private String userName;
    private String password;
    private String nickName;
    private String phone;
    private double latitude;
    private double longitude;
    private String role;
    private Timestamp createdAt;
    private boolean active = true;
}
