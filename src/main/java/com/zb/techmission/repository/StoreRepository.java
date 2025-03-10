package com.zb.techmission.repository;

import com.zb.techmission.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(String name, String location);
}
