package com.markety.platform.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDto {

    private long id;
    private String userId;
    private String password;
    private String userName;
    private double latitude;
    private double longitude;
    private String role;
    private Timestamp createdAt;
    private boolean active = true;
}
