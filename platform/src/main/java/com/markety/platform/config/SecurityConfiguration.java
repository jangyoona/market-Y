package com.markety.platform.config;

import com.markety.platform.config.security.LoginSuccessHandler;
import com.markety.platform.config.security.LogoutSuccessHandler;
import com.markety.platform.config.security.WebPasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    private final UserDetailsService userDetailsService;

    private final PersistentTokenRepository persistentTokenRepository;

    private static final String rememberMeKey = "rememberMeKey";


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/static/**").permitAll()
//                        .requestMatchers("/login-denied").permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/market/form").authenticated() // authenticated? 로그인한 사용자만 허용 설정
//                        .requestMatchers("/userView/activityPages/*noinRegisterDetail*", "/userView/activityPages/*noinRegisterList*").hasAnyRole("BOYUG", "ADMIN")
                        .anyRequest().permitAll()) // 그 외 모든 요청은 허용
                .sessionManagement(session -> session
                        .maximumSessions(1)) // 동시 세션 제어
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))

                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin((login) -> login
                        .loginPage("/account/login")
//                        .usernameParameter("userName")
//                        .passwordParameter("password")
//                        .successHandler(loginSuccessHandler)
//                        .loginProcessingUrl("/process-login")

                )

                .logout((logout) -> logout
                        .logoutUrl("/account/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                .rememberMe(rememberMe -> rememberMe
                        .key(rememberMeKey)
                        .tokenRepository(persistentTokenRepository)
                        .tokenValiditySeconds(60 * 60 * 24 * 14) // 토큰 유효기간 14일
                        .alwaysRemember(false)
                        .userDetailsService(userDetailsService)

                );

        return http.build();
    }

    // Custom Encoder
    @Bean
    PasswordEncoder passwordEncoder() {
        return new WebPasswordEncoder();
    }

    // AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder()); // PasswordEncoder 설정
        return authenticationManagerBuilder.build();
    }
}
