package com.example.care.payment.controller;

import com.example.care.membership.dto.MembershipHistoryDTO;
import com.example.care.membership.service.MembershipService;
import com.example.care.payment.dto.*;
import com.example.care.payment.service.PaymentService;
import com.example.care.security.auth.PrincipalDetails;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final WebClient webClient;
    private final MembershipService membershipService;

    @PostMapping("/kakao")
    @ResponseBody
    public ResponseEntity<KaKaoPayReadyDTO> kakaoPayStart(MemberShipDTO memberShipDTO, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String username = principal.getName();

//        유효한 멤버쉽이력 조회
        MembershipHistoryDTO membershipHistoryDTO = membershipService.findValidMembership(username);
        if (membershipHistoryDTO != null && membershipHistoryDTO.getMembership()
                .getGrade().equals(memberShipDTO.getGrade())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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

        TidDTO tidDTO = TidDTO
                .builder()
                .tid(result.getTid())
                .orderId(orderId)
                .build();
        log.debug("결제 승인때 사용할 tid 저장 로직 {}", tidDTO);
        paymentService.saveTid(tidDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/approve")
    public String payApprove(String orderId, @RequestParam("pg_token") String pgToken,
                             @AuthenticationPrincipal PrincipalDetails principalDetails, RedirectAttributes redirectAttributes) {
        log.debug("결제 승인 로직 {}", orderId);
        String tid = paymentService.findTid(orderId);
        Long userId = principalDetails.getUser().getId();
        String username = principalDetails.getUser().getUsername();
        String uri;
        MultiValueMap<String, String> param = new LinkedMultiValueMap();

        param.add("cid", "TCSUBSCRIP");
        param.add("tid", tid);
        param.add("partner_order_id", orderId);
        param.add("partner_user_id", username);
        param.add("pg_token", pgToken);

//        정기 결제 승인 api 호출
        uri = "/v1/payment/approve";
        KaKaoPayApproveDTO kaKaoPayApproveDTO = kakaoWebClient(uri, param,
                KaKaoPayApproveDTO.class);

//        결제 저장 완료후 이전 멤버쉽 있을 경우 정기결제 비활성화하기 위해 sid 반환하도록 설계
        String sid = paymentService.createPayment(kaKaoPayApproveDTO, userId);
        if (sid != null) {
            param.clear();
            param.add("cid", "TCSUBSCRIP");
            param.add("sid", sid);
            uri = "/v1/payment/manage/subscription/inactive";
            KaKaoPayDisabledDTO kaKaoPayDisabledDTO = kakaoWebClient(uri, param,
                    KaKaoPayDisabledDTO.class);
            log.debug("이전 정기결제 비활성화 결과값 {}", kaKaoPayDisabledDTO);
        }

        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "결제 완료하였습니다.", SwalIcon.SUCCESS));

        return "redirect:/membership";
    }

    @GetMapping("/cancel")
    public String payCancel(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Cancel", "결제 취소하였습니다.", SwalIcon.WARNING));

        return "redirect:/membership";
    }

    @GetMapping("/fail")
    public String payFail(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Fail", "결제 실패하였습니다.", SwalIcon.ERROR));

        return "redirect:/membership";
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
