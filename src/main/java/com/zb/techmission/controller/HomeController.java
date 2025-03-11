package com.zb.techmission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@RequestParam(required = false) String username,
                       @RequestParam(required = false) String role,
                       Model model) {

        model.addAttribute("username", username);
        model.addAttribute("role", role);

        return "index"; // templates/index.html 반환
    }
}
