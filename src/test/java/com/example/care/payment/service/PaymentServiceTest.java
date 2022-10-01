package com.example.care.payment.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.payment.domain.Payment;
import com.example.care.payment.dto.Amount;
import com.example.care.payment.dto.KaKaoPayApproveDTO;
import com.example.care.payment.repository.PaymentRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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
    private Membership membership;
    private User user;

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
    }

    @Test
    @DisplayName("정상결제 요청시 테스트")
    void paymentTest() {
        KaKaoPayApproveDTO paymentDTO = KaKaoPayApproveDTO.builder()
                .aid("요청고유번호1")
                .tid("결제고유번호1")
                .cid("Care")
                .sid("정기결제 번호 1")
                .payment_method_type("카드")
                .amount(new Amount(10000))
                .approved_at(LocalDateTime.now())
                .item_name("BRONZE")
                .partner_user_id(user.getUsername())
                .partner_order_id("상품이름 1")
                .build();
        String sid = paymentService.createPayment(paymentDTO, user.getId());
        Payment payment = paymentRepository.findBySid(paymentDTO.getSid());
        assertThat(payment.getAid()).isEqualTo(paymentDTO.getAid());
        assertThat(sid).isNull();
    }

    @Test
    @DisplayName("결제시 이전 멤버쉽 취소 처리 테스트")
    void membershipHistoryTest() {
//        given
        KaKaoPayApproveDTO paymentDTO1 = KaKaoPayApproveDTO.builder()
                .aid("요청고유번호1")
                .tid("결제고유번호1")
                .cid("Care")
                .sid("정기결제 번호 1")
                .payment_method_type("카드")
                .amount(new Amount(10000))
                .approved_at(LocalDateTime.now())
                .item_name("BRONZE")
                .partner_user_id(user.getUsername())
                .partner_order_id("상품이름 1")
                .build();
        paymentService.createPayment(paymentDTO1, user.getId());

//        when
        membership = Membership.builder()
                .grade(Grade.SILVER)
                .price(15000)
                .build();
        membershipRepository.save(membership);
        KaKaoPayApproveDTO paymentDTO2 = KaKaoPayApproveDTO.builder()
                .aid("요청고유번호2")
                .tid("결제고유번호2")
                .cid("Care")
                .sid("정기결제 번호 2")
                .payment_method_type("카드")
                .amount(new Amount(10000))
                .approved_at(LocalDateTime.now())
                .item_name("SILVER")
                .partner_user_id(user.getUsername())
                .partner_order_id("상품이름 2")
                .build();
//        결제 요청시 유효한 멤버쉽 있으면 해당 멤버쉽 정기결제번호(SID)반환하도록 설계 컨트롤러에서 정기결제 취소 api 호출
        String sid = paymentService.createPayment(paymentDTO2, user.getId());

//        then
        assertThat(sid).isEqualTo(paymentDTO1.getSid());
    }
}