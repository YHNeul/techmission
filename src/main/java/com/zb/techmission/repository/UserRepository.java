package com.zb.techmission.repository;

import com.zb.techmission.entity.StoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // ✅ 반드시 추가
public interface UserRepository extends JpaRepository<StoreUser, Long> {
    Optional<StoreUser> findByUsername(String username);
}
