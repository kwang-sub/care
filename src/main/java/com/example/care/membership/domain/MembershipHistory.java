package com.example.care.membership.domain;

import com.example.care.pay.domain.Payment;
import com.example.care.user.domain.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class MembershipHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBMERSHIP_HISTORY_ID")
    private Long id;

    @CreationTimestamp
    private LocalDateTime regDate;

    private LocalDate endDate;
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

    public void membershipCancel() {
        this.status = MembershipStatus.CANCEL;
        LocalDate now = LocalDate.now();
        if (now.getMonthValue() == 12) {
            if (regDate.getDayOfMonth() >= now.getDayOfMonth()) {
                endDate = LocalDate.of(now.getYear(), 12, regDate.getDayOfMonth());
            } else {
                endDate = LocalDate.of(now.getYear() + 1, 1, regDate.getDayOfMonth());
            }

        } else if (regDate.getDayOfMonth() >= now.getDayOfMonth()) {
            if (regDate.getDayOfMonth() >= now.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()){
                endDate = now.with(TemporalAdjusters.lastDayOfMonth());
            } else{
                endDate = LocalDate.of(now.getYear(), now.getMonth(), regDate.getDayOfMonth());
            }
        } else {
            endDate = LocalDate.of(now.getYear(), now.getMonthValue() + 1, regDate.getDayOfMonth());
        }
    }
}
