package com.example.care.membership.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private List<MembershipDetail> membershipDetails = new ArrayList<>();

    @Builder
    public Membership(Long id, Grade grade, Integer price, List<MembershipDetail> membershipDetails) {
        this.id = id;
        this.grade = grade;
        this.price = price;
        this.membershipDetails = membershipDetails;
    }
}
