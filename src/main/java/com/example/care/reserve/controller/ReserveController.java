package com.example.care.reserve.controller;

import com.example.care.membership.dto.MembershipHistoryDTO;
import com.example.care.membership.service.MembershipService;
import com.example.care.product.domain.ProductCode;
import com.example.care.product.dto.ProductDTO;
import com.example.care.reserve.dto.ReserveCancelDTO;
import com.example.care.reserve.dto.ReserveDTO;
import com.example.care.reserve.dto.ReserveTimeRequestDTO;
import com.example.care.reserve.dto.ReserveTimeResponseDTO;
import com.example.care.reserve.service.ReserveService;
import com.example.care.security.auth.PrincipalDetails;
import com.example.care.util.sweetalert.SwalIcon;
import com.example.care.util.sweetalert.SwalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReserveController {

    private final MembershipService membershipService;
    private final ReserveService reserveService;

    @GetMapping
    public String reserveForm(@ModelAttribute("productDTO") ProductDTO productDTO, Model model,
                              @AuthenticationPrincipal PrincipalDetails principalDetails, 
                              RedirectAttributes redirectAttributes) {
        Long userId = principalDetails.getUser().getId();
        MembershipHistoryDTO validMembership = membershipService.findValidMembership(userId);
        if (validMembership == null) {
            redirectAttributes.addFlashAttribute("swal",
                    new SwalMessage("Membership Service",
                            "????????? ?????? ?????? ??????????????????. ?????? ??? ??????????????????.", SwalIcon.INFO));
            return "redirect:/membership";
        }

        ReserveDTO reserveDTO = new ReserveDTO();
        reserveDTO.setProductDTO(productDTO);

        model.addAttribute("reserveDTO", reserveDTO);

        return "reserve/reserveForm";
    }

    @PostMapping
    public String reserve(@Validated ReserveDTO reserveDTO, BindingResult bindingResult,
                          @AuthenticationPrincipal PrincipalDetails principalDetails, RedirectAttributes redirectAttributes) {
        if (!reserveDTO.getProductDTO().getCode().equals(ProductCode.COUNSEL) &&
                reserveDTO.getDetailAddress().isEmpty() && reserveDTO.getPostcode().isEmpty()) {
            bindingResult.addError(new FieldError("reserveDTO",
                    "detailAddress", "????????? ??????????????????"));
        }
        
        if (bindingResult.hasErrors()) {
            log.debug("?????? validation errors = {}", bindingResult);
            return "reserve/reserveForm";
        }

        Long userId = principalDetails.getUser().getId();

        reserveService.reserve(reserveDTO, userId);
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "????????? ?????? ?????????????????????.", SwalIcon.SUCCESS));

        return "redirect:/product";
    }

    @PostMapping("/time")
    @ResponseBody
    public ReserveTimeResponseDTO confirmReserveTime(ReserveTimeRequestDTO reserveTimeRequestDTO) {
        return reserveService.confirmReserveTime(reserveTimeRequestDTO);
    }

    @PostMapping("/cancel")
    public String reserveCancel(ReserveCancelDTO reserveCancelDTO, RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getUser().getId() != reserveCancelDTO.getUserId()) {
            redirectAttributes.addFlashAttribute("swal",
                    new SwalMessage("Error", "???????????? ?????? ???????????????.", SwalIcon.ERROR));
            return "redirect:/user/reserve";
        }
        reserveService.reserveCancel(reserveCancelDTO.getReserveId());

        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "????????? ?????????????????????.", SwalIcon.SUCCESS));
        return "redirect:/user/reserve";
    }
}
