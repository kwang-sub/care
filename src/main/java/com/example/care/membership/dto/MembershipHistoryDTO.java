package com.example.care.membership.dto;

import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipStatus;
import com.example.care.pay.domain.Payment;
import com.example.care.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
public class MembershipHistoryDTO {

    private Long id;
    private LocalDateTime regDate;
    private MembershipStatus status;
    private User user;
    private Payment payment;
    private Membership membership;
}
