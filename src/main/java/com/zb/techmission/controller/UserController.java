package com.zb.techmission.controller;

import com.zb.techmission.entity.Role;
import com.zb.techmission.entity.StoreUser;
import com.zb.techmission.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String name,
                               @RequestParam String role,
                               org.springframework.ui.Model model) {
        try {
            Role userRole = Role.valueOf(role.toUpperCase()); // ENUM 변환
            StoreUser user = new StoreUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setName(name);
            user.setRole(userRole);

            StoreUser savedUser = userService.registerUser(user);

            model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다. 로그인 해주세요.");
            return "login"; // ✅ 회원가입 성공 후 login.html로 메시지와 함께 이동
        } catch (Exception e) {
            model.addAttribute("error", "회원가입 실패: " + e.getMessage());
            return "register"; // ✅ 실패 시 다시 회원가입 페이지로 이동
        }
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        org.springframework.ui.Model model) {
        Optional<StoreUser> authenticatedUser = userService.login(username, password);

        if (authenticatedUser.isPresent()) {
            model.addAttribute("username", username);
            return "redirect:/"; // ✅ 로그인 성공 시 홈으로 리디렉트
        } else {
            model.addAttribute("error", "로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login"; // ✅ 로그인 실패 시 메시지 표시
        }
    }


    // ✅ 로그인 페이지 반환
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // ✅ templates/login.html 반환
    }

    // ✅ 회원가입 페이지 반환
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // ✅ templates/register.html 반환
    }
}
