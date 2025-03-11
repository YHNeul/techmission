package com.zb.techmission.service;

import com.zb.techmission.entity.Role;
import com.zb.techmission.entity.StoreUser;
import com.zb.techmission.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ 회원가입 처리 (비밀번호 암호화 적용)
    public StoreUser registerUser(StoreUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // ✅ 비밀번호 암호화
        return userRepository.save(user);
    }

    // ✅ 로그인 처리
    public Optional<StoreUser> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword())); // ✅ 패스워드 비교
    }
}
