package com.example.care.pay.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long id;

    private String aid;
    private String cid;
    private String sid;
    private String orderId;
    private Integer price;
    private LocalDateTime regDate;

    @Builder
    public Payment(String aid, String cid, String sid, String orderId, Integer price, LocalDateTime regDate) {
        this.aid = aid;
        this.cid = cid;
        this.sid = sid;
        this.orderId = orderId;
        this.price = price;
        this.regDate = regDate;
    }
}
