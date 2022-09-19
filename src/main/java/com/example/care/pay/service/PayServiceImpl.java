package com.example.care.pay.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.domain.MembershipStatus;
import com.example.care.membership.repository.MembershipHistoryRepository;
import com.example.care.membership.repository.MembershipRepository;
import com.example.care.pay.domain.Payment;
import com.example.care.pay.domain.Tid;
import com.example.care.pay.dto.KaKaoPayApproveDTO;
import com.example.care.pay.dto.TidDTO;
import com.example.care.pay.repository.PaymentRepository;
import com.example.care.pay.repository.TidRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public String searchTid(String orderId) {
        Tid tid = tidRepository.findByOrderId(orderId);
        return tid.getTid();
    }

    @Override
    public void completePayment(KaKaoPayApproveDTO kaKaoPayApproveDTO) {
        Payment payment = Payment.builder()
                .aid(kaKaoPayApproveDTO.getAid())
                .cid(kaKaoPayApproveDTO.getCid())
                .sid(kaKaoPayApproveDTO.getSid())
                .orderId(kaKaoPayApproveDTO.getPartner_order_id())
                .price(kaKaoPayApproveDTO.getAmount().getTotal())
                .regDate(kaKaoPayApproveDTO.getApproved_at())
                .build();

        paymentRepository.save(payment);

        User user = userRepository.findByUsername(kaKaoPayApproveDTO.getPartner_user_id());

        Membership membership = membershipRepository.findByGrade(Grade.valueOf(kaKaoPayApproveDTO.getItem_name()));

        MembershipHistory membershipHistory = MembershipHistory.builder()
                .status(MembershipStatus.ORDER)
                .user(user)
                .payment(payment)
                .membership(membership)
                .build();


        membershipHistoryRepository.save(membershipHistory);
    }
}
