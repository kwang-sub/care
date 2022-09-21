package com.example.care.membership.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private Integer transportNum;
    private Integer cleanNum;
    private Integer counselNum;

    @Builder
    public Membership(Long id, Grade grade, Integer price, Integer transportNum, Integer cleanNum, Integer counselNum) {
        this.id = id;
        this.grade = grade;
        this.price = price;
        this.transportNum = transportNum;
        this.cleanNum = cleanNum;
        this.counselNum = counselNum;
    }
}
