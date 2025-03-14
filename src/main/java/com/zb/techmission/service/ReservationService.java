package com.zb.techmission.service;

import com.zb.techmission.entity.Reservation;
import com.zb.techmission.entity.Store;
import com.zb.techmission.entity.StoreUser;
import com.zb.techmission.repository.ReservationRepository;
import com.zb.techmission.repository.StoreRepository;
import com.zb.techmission.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              StoreRepository storeRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    // 예약 가능 여부 확인
    public boolean isTimeAvailable(Long storeId, LocalDate date, LocalTime time) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다: " + storeId));

        return !reservationRepository.existsByStoreAndDateAndTime(store, date, time);
    }

    // 예약 생성
    @Transactional
    public Reservation makeReservation(Long storeId, String username, LocalDate date, LocalTime time) {
        // 매장과 사용자 정보 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다: " + storeId));

        StoreUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

        // 예약 가능 여부 확인
        if (!isTimeAvailable(storeId, date, time)) {
            throw new IllegalStateException("해당 시간에는 이미 예약이 있습니다.");
        }

        System.out.println("사용자 ID: " + user.getId() + ", 이름: " + user.getUsername());

        // 예약 생성 및 저장
        Reservation reservation = new Reservation();
        reservation.setStore(store);
        reservation.setUser(user);  // user 객체 설정이 제대로 되는지 확인
        reservation.setDate(date);
        reservation.setTime(time);

        return reservationRepository.save(reservation);
    }

    // 사용자의 예약 목록 조회
    public List<Reservation> getUserReservations(String username) {
        return reservationRepository.findByUser_Username(username);
    }

    // 매장의 예약 목록 조회
    public List<Reservation> getStoreReservations(Long storeId) {
        return reservationRepository.findByStore_Id(storeId);
    }

    // 예약 취소
    @Transactional
    public void cancelReservation(Long reservationId, String username) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다: " + reservationId));

        // 예약한 사용자만 취소할 수 있도록 검증
        if (!reservation.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("예약을 취소할 권한이 없습니다.");
        }

        reservationRepository.deleteById(reservationId);
    }
}
