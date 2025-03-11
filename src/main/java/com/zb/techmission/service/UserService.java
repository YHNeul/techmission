package com.zb.techmission.service;

import com.zb.techmission.entity.Role;
import com.zb.techmission.entity.StoreUser;
import com.zb.techmission.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String username, String password, String name, Role role) {
        // ✅ 비밀번호 암호화 적용
        String encodedPassword = passwordEncoder.encode(password);
        StoreUser newUser = new StoreUser(username, encodedPassword, name, role);
        userRepository.save(newUser);
    }
}
