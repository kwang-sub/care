package com.example.care.membership.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.domain.MembershipStatus;
import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.payment.domain.Payment;
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
class MembershipServiceTest {

    @Autowired
    private MembershipService membershipService;
    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MembershipHistoryRepository membershipHistoryRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    private User user;
    private Membership membership;
    private Payment payment;

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

        payment = Payment.builder()
                .build();
        paymentRepository.save(payment);
    }

    @Test
    @DisplayName("멤버쉽 저장 테스트")
    void membershipSaveTest() {
        MembershipDTO membershipDTO = MembershipDTO.builder()
                .grade(Grade.SILVER)
                .price(15000)
                .build();
        membershipService.membershipSave(membershipDTO);
        Membership findMembership = membershipRepository.findByGrade(Grade.SILVER);
        assertThat(findMembership).isNotNull();
        assertThat(findMembership.getPrice()).isEqualTo(15000);
    }

    @Test
    @DisplayName("현재 유효한 멤버쉽 조회")
    void userValidMembershipTest() {
//        given
        MembershipHistory membershipHistory = MembershipHistory.builder()
                .user(user)
                .status(MembershipStatus.ORDER)
                .membership(membership)
                .payment(payment)
                .regDate(LocalDateTime.now())
                .build();
        membershipHistoryRepository.save(membershipHistory);

//        when
        MembershipHistory availableMembership = membershipHistoryRepository
                .findValidMembership(user.getUsername());

//        then
        assertThat(availableMembership).isNotNull();
    }

    @Test
    @DisplayName("멤버쉽 상태 변경시(CANCEL) 유효하지 않은지 테스트")
    void userNotValidMembershipTest() {
//        given
        MembershipHistory membershipHistory = MembershipHistory.builder()
                .user(user)
                .status(MembershipStatus.ORDER)
                .membership(membership)
                .payment(payment)
                .regDate(LocalDateTime.now())
                .build();
        membershipHistoryRepository.save(membershipHistory);

//        when
        membershipHistory.membershipCancel();

        MembershipHistory availableMembership = membershipHistoryRepository
                .findValidMembership(user.getUsername());

//        then
        assertThat(availableMembership).isNull();
    }
}