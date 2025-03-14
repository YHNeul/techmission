package com.zb.techmission.service;

import com.zb.techmission.entity.Store;
import com.zb.techmission.entity.StoreUser;
import com.zb.techmission.repository.StoreRepository;
import com.zb.techmission.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

    public StoreService(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    // ✅ 수정된 addStore() 메서드 (username을 통해 StoreUser를 찾아서 저장)
    @Transactional
    public Store addStore(Store store) {
        if (store.getOwner() == null) {
            throw new IllegalArgumentException("매장의 점주 정보가 없습니다.");
        }
        return storeRepository.save(store);
    }





}
