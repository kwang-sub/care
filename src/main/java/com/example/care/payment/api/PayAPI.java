package com.example.care.payment.api;

import com.example.care.membership.domain.Grade;
import com.example.care.payment.dto.KaKaoPayApproveDTO;
import com.example.care.payment.dto.KaKaoPayDisabledDTO;
import com.example.care.payment.dto.KaKaoPayReadyDTO;
import com.example.care.payment.dto.MemberShipDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayAPI {

    private final WebClient webClient;

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

    public KaKaoPayReadyDTO paymentStartAPI(MemberShipDTO memberShipDTO, Long userId) {
        log.debug("결제요청 시작 userId {}", userId);
        //        결제 시작 요청 파라미터 생성및 api 호출(webclient이용)
        String orderId = memberShipDTO.getGrade().name() + "_" + UUID.randomUUID() + "_" + userId;
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("cid", "TCSUBSCRIP");
        param.add("sid", "S1234567890987654321");
        param.add("partner_order_id", orderId);
        param.add("partner_user_id", userId.toString());
        param.add("item_name", memberShipDTO.getGrade().name());
        param.add("quantity", "1");
        param.add("total_amount", String.valueOf(memberShipDTO.getPrice()));
        param.add("tax_free_amount", "0");
        param.add("approval_url", "http://www.care.p-e.kr:8080/payment/approve?orderId=" + orderId);
        param.add("cancel_url", "http://www.care.p-e.kr:8080/payment/cancel");
        param.add("fail_url", "http://www.care.p-e.kr:8080/payment/fail");

        KaKaoPayReadyDTO kaKaoPayReadyDTO = kakaoWebClient("/v1/payment/ready", param, KaKaoPayReadyDTO.class);
        kaKaoPayReadyDTO.setOrderId(orderId);
        return kaKaoPayReadyDTO;
    }

    public KaKaoPayApproveDTO paymentApproveAPI(String tid, String orderId, String pgToken, Long userId) {
        log.debug("결제 승인 요청 api 호출 결제 고유번호 {}", tid);
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("cid", "TCSUBSCRIP");
        param.add("tid", tid);
        param.add("partner_order_id", orderId);
        param.add("partner_user_id", userId.toString());
        param.add("pg_token", pgToken);
        String uri = "/v1/payment/approve";
        return kakaoWebClient(uri, param, KaKaoPayApproveDTO.class);
    }

    public void paymentDisabledAPI(String sid) {
        log.debug("정기결제 비활성화 {}", sid);
        String uri;
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("cid", "TCSUBSCRIP");
        param.add("sid", sid);
        uri = "/v1/payment/manage/subscription/inactive";
        KaKaoPayDisabledDTO kaKaoPayDisabledDTO = kakaoWebClient(uri, param,
                KaKaoPayDisabledDTO.class);
    }

    public KaKaoPayApproveDTO paymentRegularAPI(String sid, Long userId, Integer price, Grade grade) {
        log.debug("정기결제 승인 요청 api 호출 정기결제 고유번호 {}", sid);
        String orderId = grade + "_" + UUID.randomUUID() + "_" + userId;
        MultiValueMap<String, String> param = new LinkedMultiValueMap();
        param.add("cid", "TCSUBSCRIP");
        param.add("sid", sid);
        param.add("partner_order_id", orderId);
        param.add("partner_user_id", userId.toString());
        param.add("quantity", "1");
        param.add("total_amount", price.toString());
        param.add("tax_free_amount", "0");
        String uri = "/v1/payment/subscription";
        return kakaoWebClient(uri, param, KaKaoPayApproveDTO.class);
    }
}
