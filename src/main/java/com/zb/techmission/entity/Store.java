package com.zb.techmission.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(length = 500)
    private String description;

    // 관계 매핑
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private StoreUser owner;

    // owner 컬럼을 직접 매핑하는 필드 추가
    @Column(name = "owner", nullable = false)
    private String ownerName;

}