package com.zb.techmission.service;

import com.zb.techmission.entity.Store;
import com.zb.techmission.entity.StoreUser;
import com.zb.techmission.repository.StoreRepository;
import com.zb.techmission.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public StoreService(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    // ✅ 수정된 addStore() 메서드 (username을 통해 StoreUser를 찾아서 저장)
    public Store addStore(Store store, String username) {
        StoreUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

        store.setOwner(owner);  // ✅ Store 엔티티에 점주 정보 저장
        return storeRepository.save(store);
    }
}
