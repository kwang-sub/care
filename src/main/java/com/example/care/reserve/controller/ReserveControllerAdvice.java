package com.example.care.reserve.controller;

import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import com.example.care.util.ex.exception.ReserveFullException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice(assignableTypes = {ReserveController.class})
public class ReserveControllerAdvice {
    @ExceptionHandler(ReserveFullException.class)
    public String reserveFullHandler(RedirectAttributes redirectAttributes, ReserveFullException e) {
        log.error("[exceptionHandler] ", e);
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Fail", "해당 시간에 예약이 가득차 있습니다.", SwalIcon.ERROR));
        return "redirect:/product";
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public String insufficientHandler(RedirectAttributes redirectAttributes, InsufficientAuthenticationException e) {
        log.error("[exceptionHandler] ", e);
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Fail", "당월 해당 서비스를 모두 이용하셨습니다.", SwalIcon.ERROR));
        return "redirect:/product";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String badTimeRequestHandler(RedirectAttributes redirectAttributes, IllegalStateException e) {
        log.error("[exceptionHandler] ", e);
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Fail", "예약하신 시간은 올바르지 않은 시간입니다.", SwalIcon.ERROR));
        return "redirect:/product";
    }
}
