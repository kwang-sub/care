package com.example.care.reserve.domain;

import com.example.care.membership.domain.MembershipHistory;
import com.example.care.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    @CreationTimestamp
    private LocalDateTime regDate;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "MEMBERSHIP_HISTORY_ID")
    private MembershipHistory membershipHistory;

    @Builder
    public Reserve(Long id, String name, String phoneNumber, ReserveStatus reserveStatus, LocalDate reserveDate,
                   Integer reserveTime, String postcode, String address, String detailAddress, String extraAddress,
                   Product product, MembershipHistory membershipHistory) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.reserveStatus = reserveStatus;
        this.reserveDate = reserveDate;
        ReserveTime = reserveTime;
        this.postcode = postcode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.product = product;
        this.membershipHistory = membershipHistory;
    }

    public void cancel() {
        this.reserveStatus = ReserveStatus.CANCEL;
    }
}
