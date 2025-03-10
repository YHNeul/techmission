package com.zb.techmission.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (테스트용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register", "/users/login").permitAll() // ✅ 회원가입, 로그인 페이지 접근 허용
                        .requestMatchers("/", "/css/**", "/js/**", "/img/**").permitAll() // ✅ 정적 리소스 접근 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .formLogin(login -> login
                        .loginPage("/users/login") // ✅ 로그인 페이지 설정
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 이동할 페이지
                        .failureUrl("/users/login?error=true") // ✅ 로그인 실패 시 login?error 이동
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}
