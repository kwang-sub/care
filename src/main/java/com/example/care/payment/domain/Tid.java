package com.example.care.payment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Tid {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TID_ID")
    private Long id;

    private String tid;
    private String orderId;

    @Builder
    public Tid(String tid, String orderId) {
        this.tid = tid;
        this.orderId = orderId;
    }
}
