package com.example.care.membership.domain;

import com.example.care.pay.domain.Payment;
import com.example.care.user.domain.User;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class MembershipHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBMERSHIP_HISTORY_ID")
    private Long id;

    @CreationTimestamp
    private LocalDateTime regDate;
    @Enumerated(EnumType.STRING)
    private MembershipStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToOne(optional = false)
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "MEMBERSHIP_ID")
    private Membership membership;

    @Builder
    public MembershipHistory(LocalDateTime regDate, MembershipStatus status, User user, Payment payment, Membership membership) {
        this.regDate = regDate;
        this.status = status;
        this.user = user;
        this.payment = payment;
        this.membership = membership;
    }
}
