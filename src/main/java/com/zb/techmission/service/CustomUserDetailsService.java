package com.zb.techmission.service;

import com.zb.techmission.entity.StoreUser;
import com.zb.techmission.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("🔍 로그인 시도: " + username);

        StoreUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warning("❌ 사용자 없음: " + username);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
                });

        logger.info("✅ 사용자 찾음: " + username);
        logger.info("🔑 저장된 암호화된 비밀번호: " + user.getPassword());

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // ✅ 암호화된 비밀번호 그대로 사용
                .roles(user.getRole().name())
                .build();
    }
}
