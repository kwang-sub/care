package com.example.care.membership.domain;

import com.example.care.payment.domain.Payment;
import com.example.care.product.domain.ProductCode;
import com.example.care.reserve.domain.Reserve;
import com.example.care.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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
    private Integer transportUseNum;
    private Integer cleanUseNum;
    private Integer counselUseNum;

    @OneToMany(mappedBy = "membershipHistory", cascade = CascadeType.ALL)
    private List<Reserve> reserveList = new ArrayList<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBERSHIP_ID")
    private Membership membership;

    @Builder
    public MembershipHistory(MembershipStatus status, Integer transportUseNum,
                             Integer cleanUseNum, Integer counselUseNum, User user, Payment payment, Membership membership) {
        this.status = status;
        this.transportUseNum = transportUseNum;
        this.cleanUseNum = cleanUseNum;
        this.counselUseNum = counselUseNum;
        this.user = user;
        this.payment = payment;
        this.membership = membership;
        setEndDate();
    }

    private void setEndDate() {
        LocalDate now = LocalDate.now();
        if (now.getMonthValue() == 12) {
            endDate = LocalDate.of(now.getYear() + 1, 1, now.getDayOfMonth());
            return;
        }

        LocalDate next = LocalDate.of(now.getYear(), now.getMonthValue() + 1, 1);

        if (now.getDayOfMonth() > next.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()) {
            endDate = next.with(TemporalAdjusters.lastDayOfMonth());
        } else {
            endDate = LocalDate.of(now.getYear(), now.getMonthValue() + 1, now.getDayOfMonth());
        }
    }

    public void membershipCancel() {
        this.status = MembershipStatus.CANCEL;
    }

    public void membershipComplete() {
        this.status = MembershipStatus.COMPLETE;
    }

    public void reserveProduct(ProductCode productCode) {
        if (productCode.equals(ProductCode.TRANSPORT)) {
            transportUseNum++;
        } else if (productCode.equals(ProductCode.COUNSEL)) {
            counselUseNum++;
        } else if (productCode.equals(ProductCode.CLEAN)) {
            cleanUseNum++;
        }
    }

    public void cancelProduct(ProductCode productCode) {
        if (productCode.equals(ProductCode.TRANSPORT)) {
            transportUseNum--;
        } else if (productCode.equals(ProductCode.COUNSEL)) {
            counselUseNum--;
        } else if (productCode.equals(ProductCode.CLEAN)) {
            cleanUseNum--;
        }
    }
}
