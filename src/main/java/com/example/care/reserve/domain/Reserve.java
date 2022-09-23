package com.example.care.reserve.domain;

import com.example.care.product.domain.Product;
import com.example.care.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVE_ID")
    private Long id;
    private String name;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private ReserveStatus reserveStatus;
    private LocalDate reserveDate;
    private Integer ReserveTime;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
    private LocalDateTime regDate;

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public Reserve(Long id, String name, String phoneNumber, LocalDate reserveDate, Integer reserveTime, String postcode,
                   String address, String detailAddress, String extraAddress, LocalDateTime regDate, Product product, User user) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.reserveDate = reserveDate;
        ReserveTime = reserveTime;
        this.postcode = postcode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.regDate = regDate;
        this.product = product;
        this.user = user;
    }
}
