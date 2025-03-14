package com.zb.techmission.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 매장 정보

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private StoreUser user; // 예약자 정보 (객체 참조로 변경)

    @Column(nullable = false)
    private LocalDate date; // 예약 날짜

    @Column(nullable = false)
    private LocalTime time; // 예약 시간

    @Column(nullable = false)
    private boolean confirmed = false; // 방문 확인 여부

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성 시간 자동 설정
}