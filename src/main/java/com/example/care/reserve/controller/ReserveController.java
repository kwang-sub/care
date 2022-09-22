package com.example.care.reserve.controller;

import com.example.care.membership.dto.MembershipHistoryDTO;
import com.example.care.membership.service.MembershipService;
import com.example.care.product.dto.ProductDTO;
import com.example.care.product.dto.ReserveDTO;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
    public String productReserve(@Validated ReserveDTO reserveDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.debug("예약 validation errors = {}", bindingResult);
            return "/reserve/reserveForm";
        }
        System.out.println(reserveDTO);
        return "redirect:/product";
    }
}
