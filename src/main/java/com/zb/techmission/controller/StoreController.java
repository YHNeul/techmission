package com.zb.techmission.controller;

import com.zb.techmission.entity.Store;
import com.zb.techmission.service.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public List<Store> getAllStores() {
        return storeService.getAllStores();
    }

    @PostMapping
    public Store addStore(@RequestBody Store store) {
        return storeService.addStore(store);
    }

    @GetMapping("/search")
    public List<Store> searchStores(@RequestParam String keyword) {
        return storeService.searchStores(keyword);
    }

}
