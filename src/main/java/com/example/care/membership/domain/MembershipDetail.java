package com.example.care.membership.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class MembershipDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBERSHIP_DETAIL_ID")
    private Long id;

    private String title;
    private String comment;
    private Integer number;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MEMBERSHIP_ID")
    private Membership membership;

    @Builder
    public MembershipDetail(Long id, String title, String comment, Integer number, Membership membership) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.number = number;
        this.membership = membership;
    }
}
