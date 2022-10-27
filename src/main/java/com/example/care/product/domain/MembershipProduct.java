package com.example.care.product.domain;

import com.example.care.membership.domain.Membership;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class MembershipProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODCUT_MEMBERSHIP_ID")
    private Long id;

    private Integer maxNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "MEMBERSHIP_ID")
    private Membership membership;

    @Builder
    public MembershipProduct(Integer maxNum, Product product, Membership membership) {
        this.maxNum = maxNum;
        this.product = product;
        this.membership = membership;
    }
}
