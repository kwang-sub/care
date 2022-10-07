package com.example.care.payment.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.domain.MembershipStatus;
import com.example.care.membership.repository.history.MembershipHistoryRepository;
import com.example.care.membership.repository.membership.MembershipRepository;
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

    private final WebClient webClient;


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
        String orderId = memberShipDTO.getGrade().name() + "_" + UUID.randomUUID();
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("cid", "TCSUBSCRIP");
        param.add("sid", "S1234567890987654321");
        param.add("partner_order_id", orderId);
        param.add("partner_user_id", username);
        param.add("item_name", memberShipDTO.getGrade().name());
        param.add("quantity", "1");
        param.add("total_amount", String.valueOf(memberShipDTO.getPrice()));
        param.add("tax_free_amount", "0");
        param.add("approval_url", "http://localhost:8080/payment/approve?orderId=" + orderId);
        param.add("cancel_url", "http://localhost:8080/payment/cancel");
        param.add("fail_url", "http://localhost:8080/payment/fail");

        KaKaoPayReadyDTO result = kakaoWebClient("/v1/payment/ready", param,
                KaKaoPayReadyDTO.class);
        result.setOrderId(orderId);

//        결제 승인때 사용할 고유번호 저장
        Tid tid = Tid.builder()
                .tid(result.getTid())
                .orderId(orderId)
                .build();
        tidRepository.save(tid);
        return result;
    }

    @Override
    @Transactional
    public String savePayment(KaKaoPayApproveDTO kaKaoPayApproveDTO, Long userId) {
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

    @Override
    @Transactional
    public void payApprove(String orderId, String pgToken, Long userId, String username) {
//        정기 결제 승인 api 호출
        String tid = tidRepository.findByOrderId(orderId).getTid();
        String uri;
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("cid", "TCSUBSCRIP");
        param.add("tid", tid);
        param.add("partner_order_id", orderId);
        param.add("partner_user_id", username);
        param.add("pg_token", pgToken);
        uri = "/v1/payment/approve";
        KaKaoPayApproveDTO kaKaoPayApproveDTO = kakaoWebClient(uri, param,
                KaKaoPayApproveDTO.class);
//        결제내용 저장 로직 / 이전 자동결제 내역 있을 경우 취소하기위해 정기결제번호 반환
        String sid = savePayment(kaKaoPayApproveDTO, userId);

        if (sid != null) {
            regularPaymentCancel(sid);
        }
    }

    private void regularPaymentCancel(String sid) {
        String uri;
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("cid", "TCSUBSCRIP");
        param.add("sid", sid);
        uri = "/v1/payment/manage/subscription/inactive";
        KaKaoPayDisabledDTO kaKaoPayDisabledDTO = kakaoWebClient(uri, param,
                KaKaoPayDisabledDTO.class);
        log.debug("이전 정기결제 비활성화 결과값 {}", kaKaoPayDisabledDTO);
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
                .orderId(kaKaoPayApproveDTO.getPartner_order_id())
                .price(kaKaoPayApproveDTO.getAmount().getTotal())
                .build();
    }

    private <T> T kakaoWebClient(String uri, MultiValueMap param, Class<T> type) {
        return webClient.mutate()
                .build()
                .post()
                .uri(uri)
                .bodyValue(param)
                .retrieve()
                .bodyToMono(type)
                .flux()
                .toStream()
                .findFirst()
                .orElse(null);
    }
}
