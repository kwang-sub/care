package com.example.care.payment.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.domain.MembershipStatus;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.payment.domain.Payment;
import com.example.care.payment.domain.Tid;
import com.example.care.payment.dto.KaKaoPayApproveDTO;
import com.example.care.payment.dto.TidDTO;
import com.example.care.payment.repository.PaymentRepository;
import com.example.care.payment.repository.TidRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    private final TidRepository tidRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipHistoryRepository membershipHistoryRepository;

    @Override
    @Transactional
    public void saveTid(TidDTO tidDTO) {
        Tid tid = Tid.builder()
                .tid(tidDTO.getTid())
                .orderId(tidDTO.getOrderId())
                .build();

        tidRepository.save(tid);
    }

    @Override
    public String findTid(String orderId) {
        Tid tid = tidRepository.findByOrderId(orderId);
        return tid.getTid();
    }

    @Override
    @Transactional
    public String completePayment(KaKaoPayApproveDTO kaKaoPayApproveDTO, Long userId) {
//        이전 유효한 가입내역 상태 값 변경
        MembershipHistory validMembership = membershipHistoryRepository.
                findValidMembership(kaKaoPayApproveDTO.getPartner_user_id());
        if (validMembership != null) {
            validMembership.membershipCancel();
        }

//        결제 성공으로 결제내역, 멤버쉽 가입내역 저장 로직
        Payment payment = payDTOToEntity(kaKaoPayApproveDTO);
        paymentRepository.save(payment);

        User user = userRepository.getReferenceById(userId);
        Membership membership = membershipRepository.findByGrade(Grade.valueOf(kaKaoPayApproveDTO.getItem_name()));

        MembershipHistory membershipHistory = createMembershipHistory(payment, user, membership);
        membershipHistoryRepository.save(membershipHistory);

        return validMembership != null ? validMembership.getPayment().getSid() : null;
    }

    private MembershipHistory createMembershipHistory(Payment payment, User user, Membership membership) {
        return MembershipHistory.builder()
                .status(MembershipStatus.ORDER)
                .user(user)
                .payment(payment)
                .membership(membership)
                .cleanUseNum(0)
                .transportUseNum(0)
                .counselUseNum(0)
                .regDate(LocalDateTime.now())
                .build();
    }

    private Payment payDTOToEntity(KaKaoPayApproveDTO kaKaoPayApproveDTO) {
        return Payment.builder()
                .aid(kaKaoPayApproveDTO.getAid())
                .cid(kaKaoPayApproveDTO.getCid())
                .sid(kaKaoPayApproveDTO.getSid())
                .orderId(kaKaoPayApproveDTO.getPartner_order_id())
                .price(kaKaoPayApproveDTO.getAmount().getTotal())
                .regDate(kaKaoPayApproveDTO.getApproved_at())
                .build();
    }
}
