package com.example.care.membership.controller;

import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.dto.MembershipHistoryCancelDTO;
import com.example.care.membership.service.MembershipService;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/{membershipHistoryId}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity membershipHistoryCancel(@RequestBody MembershipHistoryCancelDTO memHistoryCancelDTO) {
        System.out.println(memHistoryCancelDTO);
//        System.out.println(userId);

        return null;
    }
}
