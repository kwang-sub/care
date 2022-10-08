package com.example.care.payment.controller;

import com.example.care.membership.dto.MembershipHistoryDTO;
import com.example.care.membership.service.MembershipService;
import com.example.care.payment.dto.*;
import com.example.care.payment.service.PaymentService;
import com.example.care.security.auth.PrincipalDetails;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import com.example.care.util.ex.exception.UserAccessException;
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

    @PostMapping("/kakao")
    @ResponseBody
    public ResponseEntity<KaKaoPayReadyDTO> kakaoPayStart(MemberShipDTO memberShipDTO, Principal principal) {
        if (principal == null) {
            throw new UserAccessException("로그인 하지 않은 회원 요청");
        }
        String username = principal.getName();
        KaKaoPayReadyDTO result = paymentService.payStart(memberShipDTO, username);
        System.out.println("=============4" + result.getOrderId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/approve")
    public String payApprove(String orderId, @RequestParam("pg_token") String pgToken,
                             @AuthenticationPrincipal PrincipalDetails principalDetails, RedirectAttributes redirectAttributes) {
        Long userId = principalDetails.getUser().getId();
        String username = principalDetails.getUser().getUsername();
        System.out.println("=============3" + orderId);
        paymentService.payApprove(orderId, pgToken, userId, username);

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
}
