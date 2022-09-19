package com.example.care.pay.controller;

import com.example.care.pay.dto.KaKaoPayApproveDTO;
import com.example.care.pay.dto.KaKaoPayReadyDTO;
import com.example.care.pay.dto.MemberShipDTO;
import com.example.care.pay.dto.TidDTO;
import com.example.care.pay.service.PayService;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;
    private final WebClient webClient;

    @PostMapping("/pay/kakao")
    @ResponseBody
    public ResponseEntity<KaKaoPayReadyDTO> kakaoPayStart(MemberShipDTO MemberShipDTO, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String userId = principal.getName();
        String orderId = userId + "_" + MemberShipDTO.getGrade().name() + "_" + UUID.randomUUID();
        String parameter = "cid=TCSUBSCRIP&" + //가맹점코드
                "sid=S1234567890987654321&" + //정기결제 키
                "partner_order_id=" + orderId + "&" + //가맹점 주문번호
                "partner_user_id=" + userId + "&" + //가맹점회원 id
                "item_name=" + MemberShipDTO.getGrade().name() + "&" + //상품명
                "quantity=1&" + //상품수량
                "total_amount=" + MemberShipDTO.getPrice() + "&" +//상품총액
                "tax_free_amount=0&" +//상품 비과세 금액
                "approval_url=http://localhost:8080/pay/ok?orderId=" + orderId + "&" + //성공 url
                "cancel_url=http://localhost:8080/pay/cancel&" + //취소 url
                "fail_url=http://localhost:8080/pay/fail";//실패 url

        KaKaoPayReadyDTO result = webClient.mutate()
                .build()
                .post()
                .uri("/v1/payment/ready")
                .bodyValue(parameter)
                .retrieve()
                .bodyToMono(KaKaoPayReadyDTO.class)
                .flux()
                .toStream()
                .findFirst()
                .orElse(null);
        result.setOrderId(orderId);

        TidDTO tidDTO = TidDTO
                .builder()
                .tid(result.getTid())
                .orderId(orderId)
                .build();
        log.debug("결제 승인때 사용할 tid 저장 로직 {}", tidDTO);
        payService.saveTid(tidDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/pay/ok")
    public String payApprove(String orderId, @RequestParam("pg_token") String pgToken, Principal principal,
                             RedirectAttributes redirectAttributes) {
        log.debug("결제 승인 로직 {}", orderId);
        String tid = payService.searchTid(orderId);
        String username = principal.getName();

        String parameter = "cid=TCSUBSCRIP&" +
                "tid=" + tid + "&" +
                "partner_order_id=" + orderId + "&" +
                "partner_user_id=" + username + "&" +
                "pg_token=" + pgToken;

//        정기 결제 승인 api 호출
        KaKaoPayApproveDTO kaKaoPayApproveDTO = webClient.mutate()
                .build()
                .post()
                .uri("/v1/payment/approve")
                .bodyValue(parameter)
                .retrieve()
                .bodyToMono(KaKaoPayApproveDTO.class)
                .flux()
                .toStream()
                .findFirst()
                .orElse(null);

        payService.completePayment(kaKaoPayApproveDTO);

        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "결제 완료하였습니다.", SwalIcon.SUCCESS));

        return "redirect:/membership";
    }

    @GetMapping("/pay/cancel")
    public String payCancel(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Cancel", "결제 취소하였습니다.", SwalIcon.WARNING));

        return "redirect:/membership";
    }

    @GetMapping("/pay/fail")
    public String payFail(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Fail", "결제 실패하였습니다.", SwalIcon.ERROR));

        return "redirect:/membership";
    }
}
