package com.markety.platform.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BoardAttachDto {
    private long id;
    private long boardId;
    private String originName;
    private String savedName;
    private Timestamp createdAt;


}
