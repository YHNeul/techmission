package com.zb.techmission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Spring Boot + Thymeleaf 프로젝트입니다!");
        return "index"; // templates/index.html 반환
    }
}
