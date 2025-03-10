package com.zb.techmission.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 매장 이름

    @Column(nullable = false)
    private String location; // 매장 위치

    @Column(length = 500)
    private String description; // 매장 설명

    @Column(nullable = false)
    private String owner; // 점주 ID
}
