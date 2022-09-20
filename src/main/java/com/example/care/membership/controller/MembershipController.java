package com.example.care.membership.controller;

import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.service.MembershipService;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping("/membership")
    public String membershipPage(Model model) {

        List<MembershipDTO> membershipList = membershipService.membershipList();
        model.addAttribute("membershipList", membershipList);

        return "/membership/list";
    }

    @GetMapping("/membership/dup")
    public String membershipDup(RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Duplication", "이미 가입하신 멤버쉽 입니다.", SwalIcon.INFO));
        return "redirect:/membership";
    }
}
