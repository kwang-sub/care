package com.example.care.product.domain;

import com.example.care.membership.domain.Membership;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ProductMembership {

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
    public ProductMembership(Integer maxNum, Product product, Membership membership) {
        this.maxNum = maxNum;
        this.product = product;
        this.membership = membership;
    }
}
