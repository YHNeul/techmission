package com.zb.techmission.repository;

import com.zb.techmission.entity.Reservation;
import com.zb.techmission.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser_Username(String username);
    List<Reservation> findByStore_Id(Long storeId);

    // 특정 매장, 날짜, 시간에 이미 예약이 있는지 확인하는 메서드
    boolean existsByStoreAndDateAndTime(Store store, LocalDate date, LocalTime time);

    List<Reservation> findByStoreIdAndDateAndUserUsername(Long storeId, LocalDate date, String username);

    // 방문 확인된 특정 매장, 특정 사용자의 예약 찾기
    List<Reservation> findByStoreIdAndUserUsernameAndConfirmedTrue(Long storeId, String username);
}