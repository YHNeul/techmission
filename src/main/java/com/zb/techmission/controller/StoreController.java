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
    public String showStoreRegisterPage(@RequestParam String username, Model model) {
        StoreUser user = userService.getUserByUsername(username);

        if (!user.getRole().equals(Role.OWNER)) {
            return "redirect:/stores?username=" + username;
        }

        model.addAttribute("username", username);
        model.addAttribute("store", new Store());
        return "store_register";
    }

    // ✅ 매장 등록 처리 (owner_id 설정)
    @PostMapping("/register")
    public String addStore(@ModelAttribute Store store, @RequestParam String owner_id) {
        StoreUser owner = userService.getUserByUsername(owner_id);
        if (owner == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + owner_id);
        }
        store.setOwner(owner);  // owner_id 관계 설정
        store.setOwnerName(owner.getUsername());  // owner 컬럼에 사용자 이름 설정
        storeService.addStore(store);
        return "redirect:/stores?username=" + owner_id + "&role=ROLE_" + owner.getRole();
    }

    // 매장 상세 조회
    @GetMapping("/{id}")
    public String getStoreDetail(@PathVariable Long id,
                                 @RequestParam String username,
                                 @RequestParam String role,
                                 Model model) {
        Store store = storeService.getStoreById(id);
        if (store == null) {
            return "redirect:/stores?username=" + username + "&role=" + role;
        }

        model.addAttribute("store", store);
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        return "store_detail";
    }





}
