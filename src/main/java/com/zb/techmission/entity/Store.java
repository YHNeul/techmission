package com.zb.techmission.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    // ✅ StoreUser 객체로 변경 (점주 정보 저장)
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private StoreUser owner;

    public void setOwner(StoreUser owner) {
        this.owner = owner;
    }

    public StoreUser getOwner() {
        return owner;
    }
}
