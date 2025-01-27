package com.markety.platform.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class BoardDto {

    private long id;
    private long writer;
    private String title;
    private String content;
    private int price;
    private double latitude;
    private double longitude;
    private Timestamp createdAt;
    private boolean active = true;


    private List<BoardAttachDto> attach;

}
