package com.markety.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${board-attach-dir}")
    private String boardAttachDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 별도의 static 경로 지정
        // ( imgae, css, js, html 등 controller를 거치지 않고 요청할 수 있는 경로 )
//        String currentDir = System.getProperty("user.dir");
//        File uploadDir = new File(currentDir, "upload");
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();
//        }

        // 로컬에서 돌리는거라 바꿔줌 [ D:\abc\def -> file:D:\abc\def ]
        registry
                .addResourceHandler(boardAttachDir + "**") // 웹 요청 경로
                .addResourceLocations("file:" + boardAttachDir); // 실제 파일 경로

    }
}
