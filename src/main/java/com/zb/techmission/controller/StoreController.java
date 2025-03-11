package com.zb.techmission.controller;

import com.zb.techmission.entity.Role;
import com.zb.techmission.entity.Store;
import com.zb.techmission.entity.StoreUser;
import com.zb.techmission.service.StoreService;
import com.zb.techmission.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/stores")
public class StoreController {
    private final StoreService storeService;
    private final UserService userService;

    public StoreController(StoreService storeService, UserService userService) {
        this.storeService = storeService;
        this.userService = userService;
    }

    // ✅ 매장 목록 조회 (로그인한 사용자만 볼 수 있음)
    @GetMapping
    public String getAllStores(@RequestParam String username, @RequestParam String role, Model model) {
        StoreUser user = userService.getUserByUsername(username);

        // 🔥 ROLE_ 접두사 제거
        String roleWithoutPrefix = role.replace("ROLE_", "");

        model.addAttribute("username", username);
        model.addAttribute("role", roleWithoutPrefix);

        List<Store> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);

        return "store_list";
    }

    // ✅ 매장 등록 페이지 (파트너만 접근 가능)
    @GetMapping("/register")
    public String showStoreRegisterPage(@RequestParam String username, @RequestParam String role, Model model) {
        // 🔥 ROLE_ 접두사 제거 후 검사
        String roleWithoutPrefix = role.replace("ROLE_", "");

        if (!roleWithoutPrefix.equals("OWNER")) {
            return "redirect:/stores?username=" + username + "&role=" + roleWithoutPrefix;
        }

        model.addAttribute("username", username);
        model.addAttribute("role", roleWithoutPrefix);
        model.addAttribute("store", new Store());
        return "store_register";
    }

    // ✅ 매장 등록 (파트너만 가능)
    @PostMapping("/register")
    public String addStore(@ModelAttribute Store store, @RequestParam String username, @RequestParam String role) {
        // 🔥 ROLE_ 접두사 제거 후 검사
        String roleWithoutPrefix = role.replace("ROLE_", "");

        if (!roleWithoutPrefix.equals("OWNER")) {
            return "redirect:/stores?username=" + username + "&role=" + roleWithoutPrefix;
        }

        storeService.addStore(store, username);
        return "redirect:/stores?username=" + username + "&role=" + roleWithoutPrefix;
    }
}
