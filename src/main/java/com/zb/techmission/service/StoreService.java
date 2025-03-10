package com.zb.techmission.service;

import com.zb.techmission.entity.Store;
import com.zb.techmission.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Store addStore(Store store) {
        return storeRepository.save(store);
    }

    public List<Store> searchStores(String keyword) {
        return storeRepository.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(keyword, keyword);
    }

}