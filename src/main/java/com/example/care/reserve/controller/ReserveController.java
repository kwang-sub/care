package com.example.care.reserve.controller;

import com.example.care.membership.dto.MembershipHistoryDTO;
import com.example.care.membership.service.MembershipService;
import com.example.care.product.domain.ProductCode;
import com.example.care.product.dto.ProductDTO;
import com.example.care.reserve.dto.ReserveConfirmDTO;
import com.example.care.reserve.dto.ReserveDTO;
import com.example.care.reserve.service.ReserveService;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReserveController {

    private final MembershipService membershipService;
    private final ReserveService reserveService;

    @GetMapping
    public String reserveForm(@ModelAttribute("reserveDTO") ReserveDTO reserveDTO, ProductDTO productDTO,
                              Principal principal, RedirectAttributes redirectAttributes) {
        MembershipHistoryDTO validMembership = membershipService.findValidMembership(principal.getName());
        if (validMembership == null) {
            redirectAttributes.addFlashAttribute("swal",
                    new SwalMessage("Membership Service",
                            "멤버쉽 회원 전용 서비스입니다. 가입 후 이용해주세요.", SwalIcon.INFO));
            return "redirect:/membership";
        }

        reserveDTO.setProductDTO(productDTO);
        return "/reserve/reserveForm";
    }

    @PostMapping
    public String reserve(@Validated ReserveDTO reserveDTO, BindingResult bindingResult) {
        if (!reserveDTO.getProductDTO().getCode().equals(ProductCode.COUNSEL) && 
                reserveDTO.getDetailAddress().isEmpty() && reserveDTO.getPostcode().isEmpty()) {
            bindingResult.addError(new FieldError("reserveDTO",
                    "detailAddress", "주소를 입력해주세요"));
        }
        
        if (bindingResult.hasErrors()) {
            log.debug("예약 validation errors = {}", bindingResult);
            return "/reserve/reserveForm";
        }

        reserveService.reserve(reserveDTO);


        return "redirect:/product";
    }

    @PostMapping("/confirm")
    public void reserveConfirm(ReserveConfirmDTO reserveConfirmDTO) {
        System.out.println(reserveConfirmDTO);
    }
}
