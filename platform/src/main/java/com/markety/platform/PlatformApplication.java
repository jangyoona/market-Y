package com.markety.platform;

import io.github.cdimascio.dotenv.Dotenv;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.markety.platform.mapper")
public class PlatformApplication {

	public static void main(String[] args) {

		// Spring Environment 객체에 Env 파일 내용 전체 등록
		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		// 개별 등록
		// System.setProperty("DB_URL", dotenv.get("DB_URL"));

		SpringApplication.run(PlatformApplication.class, args);
	}

}