package com.example.care.membership.domain;

import com.example.care.product.domain.MembershipProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBERSHIP_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private Integer price;

    @OneToMany(mappedBy = "membership")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<MembershipProduct> membershipProductList = new ArrayList<>();

    @OneToMany(mappedBy = "membership")
    private List<MembershipHistory> membershipHistoryList = new ArrayList<>();

    @Builder
    public Membership(Long id, Grade grade, Integer price) {
        this.id = id;
        this.grade = grade;
        this.price = price;
    }
}
