package com.example.care.membership.controller;

import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
