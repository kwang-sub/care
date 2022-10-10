package com.example.care.membership.controller;

import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.service.MembershipService;
import com.example.care.security.auth.PrincipalDetails;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping()
    public String membershipPage(Model model) {

        List<MembershipDTO> membershipList = membershipService.membershipList();
        model.addAttribute("membershipList", membershipList);

        return "/membership/list";
    }

    @PostMapping("/cancel")
    public String membershipHistoryCancel(Long userId, RedirectAttributes redirectAttributes,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null || principalDetails.getUser().getId() != userId) {
            redirectAttributes.addFlashAttribute("swal",
                    new SwalMessage("Error", "올바르지 않은 요청입니다.", SwalIcon.ERROR));
            return "redirect:/user/myInfo";
        }
        membershipService.userMembershipCancel(userId);

        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "멤버쉽이 해지되었습니다..", SwalIcon.SUCCESS));

        return "redirect:/user/myInfo";
    }
}
