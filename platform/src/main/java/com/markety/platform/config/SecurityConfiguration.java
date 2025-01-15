package com.markety.platform.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/static/**").permitAll()
//                        .requestMatchers("/login-denied").permitAll()
//                        .requestMatchers("/board/**").authenticated() // authenticated? 로그인한 사용자만 허용 설정
//                        .requestMatchers("/chat/**", "/moveChatting/**").authenticated()
//                        .requestMatchers("/userView/activityPages/*noinRegisterDetail*", "/userView/activityPages/*noinRegisterList*").hasAnyRole("BOYUG", "ADMIN")
//                        .requestMatchers("/adminView/**", "/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/myPage/**").hasRole("BOYUG")
//                        .requestMatchers("/personal/personalHome").hasRole("USER")
                        .anyRequest().permitAll()) // 그 외 모든 요청은 허용
                .sessionManagement(session -> session
                        // jwt: 세션 관리 상태 없음으로 구성 - 세션 생성 or 사용x
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();

    }
}
