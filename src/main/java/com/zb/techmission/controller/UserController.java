package com.zb.techmission.controller;

import com.zb.techmission.entity.Role;
import com.zb.techmission.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // templates/login.html 반환
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";  // templates/register.html 반환
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String name,
                               @RequestParam Role role) {
        userService.registerUser(username, password, name, role);
        return "redirect:/users/login"; // 회원가입 후 로그인 페이지로 이동
    }
}
