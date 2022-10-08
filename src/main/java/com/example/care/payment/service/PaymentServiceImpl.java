package com.example.care.payment.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.domain.MembershipStatus;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
import com.example.care.payment.api.PayAPI;
import com.example.care.payment.domain.Payment;
import com.example.care.payment.domain.Tid;
import com.example.care.payment.dto.*;
import com.example.care.payment.repository.PaymentRepository;
import com.example.care.payment.repository.TidRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.ex.exception.DuplicateMembershipException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TidRepository tidRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipHistoryRepository membershipHistoryRepository;
    private final PayAPI payAPI;

    @Override
    @Transactional
    public KaKaoPayReadyDTO payStart(MemberShipDTO memberShipDTO, String username) {
//        결제 요청 멤버쉽과 똑같은 멤버쉽 가입이력확인
        MembershipHistory membershipHistory = membershipHistoryRepository.findValidMembership(username);
        if (membershipHistory != null && membershipHistory.getMembership()
                .getGrade().equals(memberShipDTO.getGrade())) {
            throw new DuplicateMembershipException("똑같은 멤버쉽 요청");
        }

//        결제 시작 요청 파라미터 생성및 api 호출(webclient이용)
        KaKaoPayReadyDTO result = payAPI.paymentStartAPI(memberShipDTO, username);
//        결제 승인때 사용할 고유번호 저장
        Tid tid = Tid.builder()
                .tid(result.getTid())
                .orderId(result.getOrderId())
                .build();
        tidRepository.save(tid);
        return result;
    }

    @Override
    @Transactional
    public void payApprove(String orderId, String pgToken, Long userId, String username) {
//        정기 결제 승인 api 호출
        String tid = tidRepository.findByOrderId(orderId).getTid();
        KaKaoPayApproveDTO kaKaoPayApproveDTO = payAPI.paymentApproveAPI(tid, orderId, pgToken, username);

//        이전 유효한 가입내역 취소
        MembershipHistory validMembership = membershipHistoryRepository.
                findValidMembership(kaKaoPayApproveDTO.getPartner_user_id());
        if (validMembership != null) {
//            결제 환불 로직 작성해야됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            validMembership.membershipCancel();
//            정기결제 비활성화 api호출
            payAPI.paymentDisabledAPI(validMembership.getPayment().getSid());
        }

//        결제 성공으로 결제내역, 멤버쉽 가입내역 저장 로직
        Payment payment = payDTOToEntity(kaKaoPayApproveDTO);
        paymentRepository.save(payment);

        User user = userRepository.getReferenceById(userId);
        Membership membership = membershipRepository.findByGrade(Grade.valueOf(kaKaoPayApproveDTO.getItem_name()));

        MembershipHistory membershipHistory = createMembershipHistory(payment, user, membership);
        membershipHistoryRepository.save(membershipHistory);
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
                .build();
    }

    private Payment payDTOToEntity(KaKaoPayApproveDTO kaKaoPayApproveDTO) {
        return Payment.builder()
                .aid(kaKaoPayApproveDTO.getAid())
                .cid(kaKaoPayApproveDTO.getCid())
                .sid(kaKaoPayApproveDTO.getSid())
                .tid(kaKaoPayApproveDTO.getTid())
                .orderId(kaKaoPayApproveDTO.getPartner_order_id())
                .price(kaKaoPayApproveDTO.getAmount().getTotal())
                .build();
    }
}
