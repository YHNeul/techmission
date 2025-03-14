package com.zb.techmission.service;

import com.zb.techmission.entity.Reservation;
import com.zb.techmission.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KioskService {
    private final ReservationRepository reservationRepository;

    public KioskService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // 특정 매장에 대한 오늘의 현재 사용자 예약 찾기
    public Reservation findActiveReservation(Long storeId, String username) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // 오늘 날짜의 예약 중 현재 시간 기준 30분 전후의 예약만 조회
        List<Reservation> todayReservations = reservationRepository.findByStoreIdAndDateAndUserUsername(
                storeId, today, username);

        // 현재 시간 기준으로 적합한 예약 필터링 (예약 시간 30분 전부터 30분 후까지 체크인 가능)
        List<Reservation> activeReservations = todayReservations.stream()
                .filter(r -> {
                    long minutesDiff = ChronoUnit.MINUTES.between(now, r.getTime());
                    return minutesDiff >= -30 && minutesDiff <= 30; // 예약 시간 전후 30분
                })
                .filter(r -> !r.isConfirmed()) // 아직 확인되지 않은 예약만
                .collect(Collectors.toList());

        if (activeReservations.isEmpty()) {
            throw new IllegalStateException("현재 시간에 유효한 예약을 찾을 수 없습니다.");
        }

        // 가장 가까운 시간의 예약 반환
        return activeReservations.stream()
                .min((r1, r2) -> {
                    long diff1 = Math.abs(ChronoUnit.MINUTES.between(now, r1.getTime()));
                    long diff2 = Math.abs(ChronoUnit.MINUTES.between(now, r2.getTime()));
                    return Long.compare(diff1, diff2);
                })
                .orElseThrow(() -> new IllegalStateException("현재 시간에 유효한 예약을 찾을 수 없습니다."));
    }

    // 예약 ID로 예약 정보 조회
    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다: " + reservationId));
    }

    // 체크인 가능 여부 확인 (예약 시간 10분 전부터 가능)
    public boolean canCheckIn(Reservation reservation) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // 예약 날짜가 오늘이 아니면 체크인 불가
        if (!reservation.getDate().equals(today)) {
            return false;
        }

        // 예약 시간과 현재 시간의 차이 계산 (분 단위)
        long minutesDiff = ChronoUnit.MINUTES.between(now, reservation.getTime());

        // 예약 시간 10분 전부터 30분 후까지 체크인 가능
        return minutesDiff <= 10 && minutesDiff >= -30;
    }

    // 체크인 처리
    @Transactional
    public void checkIn(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);

        if (reservation.isConfirmed()) {
            throw new IllegalStateException("이미 체크인 완료된 예약입니다.");
        }

        if (!canCheckIn(reservation)) {
            throw new IllegalStateException("지금은 체크인할 수 없습니다. 예약 시간 10분 전부터 가능합니다.");
        }

        // 체크인 완료 처리
        reservation.setConfirmed(true);
        reservationRepository.save(reservation);
    }
}