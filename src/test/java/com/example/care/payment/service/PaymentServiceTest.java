package com.example.care.payment.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.payment.api.PayAPI;
import com.example.care.payment.domain.Payment;
import com.example.care.payment.domain.Tid;
import com.example.care.payment.dto.Amount;
import com.example.care.payment.dto.KaKaoPayApproveDTO;
import com.example.care.payment.repository.PaymentRepository;
import com.example.care.payment.repository.TidRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private MembershipHistoryRepository membershipHistoryRepository;
    @Autowired
    private TidRepository tidRepository;
    private Membership membership;
    private User user;
    @MockBean
    private PayAPI payAPI;
    Tid tid;
    String sid = "정기결제 번호";

    @BeforeEach
    void setup() {
        user = User.builder()
                .username("test")
                .build();
        userRepository.save(user);

        membership = Membership.builder()
                .grade(Grade.BRONZE)
                .price(10000)
                .build();
        membershipRepository.save(membership);
        tid = Tid.builder()
                .orderId("orderId")
                .tid("tid")
                .build();
        tidRepository.save(tid);
        given(payAPI.paymentApproveAPI(any(), any(), any(), any()))
                .willReturn(KaKaoPayApproveDTO.builder()
                        .aid("요청고유번호1")
                        .tid("결제고유번호1")
                        .cid("Care")
                        .sid(sid)
                        .payment_method_type("카드")
                        .amount(new Amount(10000))
                        .approved_at(LocalDateTime.now())
                        .item_name("BRONZE")
                        .partner_user_id(user.getId().toString())
                        .partner_order_id(tid.getOrderId())
                        .build()
                );
    }

    @Test
    @DisplayName("정상결제 요청시 테스트")
    void paymentTest() {

        paymentService.payApprove(tid.getOrderId(), "token", user.getId());

        Payment payment = paymentRepository.findBySid(sid);
        assertThat(payment.getOrderId()).isEqualTo(tid.getOrderId());

        MembershipHistory findMembershipHistory = membershipHistoryRepository.findMembershipHistoryByUserId(user.getId());
        assertThat(findMembershipHistory.getPayment().getId()).isEqualTo(payment.getId());
    }

    @Test
    @DisplayName("결제시 이전 멤버쉽 취소 처리 테스트")
    void membershipHistoryTest() {
        paymentService.payApprove(tid.getOrderId(), "token", user.getId());
        paymentService.payApprove(tid.getOrderId(), "token", user.getId());
//        기존 정기결제 비활성화메서드 호출되는지 확인
        verify(payAPI, times(1)).paymentDisabledAPI(any());
    }
}