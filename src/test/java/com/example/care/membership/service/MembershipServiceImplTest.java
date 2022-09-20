package com.example.care.membership.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.domain.MembershipStatus;
import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.dto.MembershipDetailDTO;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.pay.domain.Payment;
import com.example.care.pay.repository.PaymentRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    @DisplayName("멤버쉽 저장 테스트")
    void membershipSaveTest() {
        //given
        List<MembershipDetailDTO> membershipDetailDTOS = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            membershipDetailDTOS.add(MembershipDetailDTO.builder()
                    .title("서비스명")
                    .comment("설명")
                    .number(5)
                    .build());
        }

        MembershipDTO membershipDTO = MembershipDTO.builder()
                .grade(Grade.BRONZE)
                .price(10000)
                .membershipDetailDTOs(membershipDetailDTOS)
                .build();

        //when
        membershipService.membershipSave(membershipDTO);

        //then
        List<MembershipDTO> membershipDTOS = membershipService.membershipList();
        assertThat(membershipDTOS.size()).isEqualTo(1);

        MembershipDTO findDTO = membershipDTOS.get(0);
        assertThat(findDTO.getGrade()).isEqualTo(membershipDTO.getGrade());
        assertThat(findDTO.getMembershipDetailDTOs().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("현재 유효한 멤버쉽 조회")
    void userValidMembershipTest() {
//        given
        User user = User.builder()
                .username("test")
                .build();
        userRepository.save(user);

        Membership membership = Membership.builder()
                .grade(Grade.BRONZE)
                .price(10000)
                .build();
        membershipRepository.save(membership);

        Payment payment = Payment.builder()
                .build();
        paymentRepository.save(payment);

        MembershipHistory membershipHistory = MembershipHistory.builder()
                .user(user)
                .status(MembershipStatus.ORDER)
                .membership(membership)
                .payment(payment)
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
        User user = User.builder()
                .username("test")
                .build();
        userRepository.save(user);

        Membership membership = Membership.builder()
                .grade(Grade.BRONZE)
                .price(10000)
                .build();
        membershipRepository.save(membership);

        Payment payment = Payment.builder()
                .build();
        paymentRepository.save(payment);

        MembershipHistory membershipHistory = MembershipHistory.builder()
                .user(user)
                .status(MembershipStatus.ORDER)
                .membership(membership)
                .payment(payment)
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