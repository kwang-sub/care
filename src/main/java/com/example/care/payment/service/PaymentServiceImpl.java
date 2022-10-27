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
import com.example.care.payment.dto.KaKaoPayApproveDTO;
import com.example.care.payment.dto.KaKaoPayReadyDTO;
import com.example.care.payment.dto.MemberShipDTO;
import com.example.care.payment.repository.PaymentRepository;
import com.example.care.payment.repository.TidRepository;
import com.example.care.user.domain.User;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.ex.exception.DuplicateMembershipException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
    public KaKaoPayReadyDTO payStart(MemberShipDTO memberShipDTO, Long userId) {
//        결제 요청 멤버쉽과 똑같은 멤버쉽 가입이력확인
        MembershipHistory membershipHistory = membershipHistoryRepository.findValidMembership(userId);
        if (membershipHistory != null && membershipHistory.getMembership()
                .getGrade().equals(memberShipDTO.getGrade())) {
            throw new DuplicateMembershipException("똑같은 멤버쉽 요청");
        }

        Membership findMembership = membershipRepository.findByGrade(memberShipDTO.getGrade());
        memberShipDTO.setPrice(findMembership.getPrice());
//        결제 시작 요청 파라미터 생성및 api 호출(webclient이용)
        KaKaoPayReadyDTO result = payAPI.paymentStartAPI(memberShipDTO, userId);
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
    public void payApprove(String orderId, String pgToken, Long userId) {
//        정기 결제 승인 api 호출
        String tid = tidRepository.findByOrderId(orderId).getTid();
        KaKaoPayApproveDTO kaKaoPayApproveDTO = payAPI.paymentApproveAPI(tid, orderId, pgToken, userId);

//        이전 유효한 가입내역 취소
        MembershipHistory validMembership = membershipHistoryRepository.
                findValidMembership(Long.valueOf(kaKaoPayApproveDTO.getPartner_user_id()));
        if (validMembership != null) {
//            정기결제 비활성화 api호출
            payAPI.paymentDisabledAPI(validMembership.getPayment().getSid());
            validMembership.membershipCancel();
        }

//        결제 성공으로 결제내역, 멤버쉽 가입내역 저장 로직
        Payment payment = payDTOToEntity(kaKaoPayApproveDTO);
        paymentRepository.save(payment);

        User user = userRepository.getReferenceById(userId);
        Membership membership = membershipRepository.findByGrade(Grade.valueOf(kaKaoPayApproveDTO.getItem_name()));

        MembershipHistory membershipHistory = createMembershipHistory(payment, user, membership);
        membershipHistoryRepository.save(membershipHistory);
    }

    @Override
    @Transactional
    public void membershipCompleteSch(LocalDate now) {
        List<MembershipHistory> membershipHistoryList = membershipHistoryRepository.
                findCompleteMembershipHistory(now);
        membershipHistoryList.forEach( membershipHistory -> {
            membershipHistory.membershipComplete();
//            멤버쉽금액 변경 가능성있기에 해당 멤버쉽 재조회해서 가격
            Grade grade = membershipHistory.getMembership().getGrade();
            Membership findMembership = membershipRepository.findByGrade(grade);

            User user = membershipHistory.getUser();
            Integer price = findMembership.getPrice();
            String sid = membershipHistory.getPayment().getSid();
            KaKaoPayApproveDTO kaKaoPayApproveDTO = payAPI.paymentRegularAPI(sid, user.getId(), price, grade);

//            api 호출 후 DB 저장로직
            Payment payment = payDTOToEntity(kaKaoPayApproveDTO);
            paymentRepository.save(payment);

            MembershipHistory newMembershipHistory = createMembershipHistory(payment, user, findMembership);
            membershipHistoryRepository.save(newMembershipHistory);
        });
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
