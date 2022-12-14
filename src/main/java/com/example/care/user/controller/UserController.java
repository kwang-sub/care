package com.example.care.user.controller;

import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveListDTO;
import com.example.care.reserve.service.ReserveService;
import com.example.care.security.auth.PrincipalDetails;
import com.example.care.user.dto.UserDTO;
import com.example.care.user.dto.UserInfoDTO;
import com.example.care.user.dto.UserProfileDTO;
import com.example.care.user.dto.UserPwDTO;
import com.example.care.user.service.UserService;
import com.example.care.util.sweetalert.SwalIcon;
import com.example.care.util.sweetalert.SwalMessage;
import com.example.care.util.ex.exception.DuplicateUserException;
import com.example.care.util.ex.exception.UserAccessException;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;
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

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReserveService reserveService;

    @GetMapping("/login")
    public String userLoginForm(@ModelAttribute("userDTO") UserDTO userDTO, HttpSession session) {
        if (session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            return "redirect:/";
        }

        return "user/login";
    }

    @GetMapping("/join")
    public String userJoinForm(@ModelAttribute("userDTO") UserDTO userDTO, HttpSession session) {
        if (session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            return "redirect:/";
        }

        return "user/join";
    }

    @GetMapping("/profile")
    public String userProfileModifyForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Long userId = principalDetails.getUser().getId();
        model.addAttribute("userProfileDTO", userService.getUserProfile(userId));
        return "user/profile";
    }

    @PostMapping("/{userId}/profile")
    public String userProfileModify(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("userId") Long userId,
                                    @Validated UserProfileDTO userProfileDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("???????????? ?????? validation errors = {}", bindingResult);
            return "user/profile";
        }
        if (principalDetails.getUser().getId() != userId) {
            log.error("????????? ???????????? ?????? ?????? ???????????? ??????");
            redirectAttributes.addFlashAttribute("swal",
                    new SwalMessage("Error", "????????? ???????????????.", SwalIcon.ERROR));
            return "redirect:/user/profile";
        }
        userService.profileModify(userProfileDTO);
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "???????????? ?????? ?????????????????????.", SwalIcon.SUCCESS));

        return "redirect:/user/profile";
    }

    @GetMapping("/password")
    public String userPwModifyForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Long userId = principalDetails.getUser().getId();
        UserPwDTO userPwDTO = new UserPwDTO();
        userPwDTO.setUserId(userId);
        model.addAttribute("userPwDTO", userPwDTO);
        return "user/password";
    }

    @PostMapping("/{userId}/password")
    public String userPasswordModify(@PathVariable("userId")Long userId, @AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @Validated UserPwDTO userPwDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Long loginUserId = principalDetails.getUser().getId();

        if (!userPwDTO.getModPw().equals(userPwDTO.getCheckModPw())) {
            bindingResult.addError(new FieldError("userPwDTO", "checkModPw",
                    "???????????? ?????? ?????? ?????????????????????."));
        }
        if (bindingResult.hasErrors()) {
            log.info("???????????? ?????? validation errors = {}", bindingResult);
            return "user/password";
        }
        if (loginUserId != userId) {
            log.info("??????????????? ?????? ?????? ?????? ???????????? ????????????");
            redirectAttributes.addFlashAttribute("swal",
                    new SwalMessage("error", "????????? ???????????????.", SwalIcon.ERROR));
            return "redirect:/user/password";
        }

        try {
            userService.passwordModify(userPwDTO);
        } catch (UserAccessException e) {
            log.error("????????? ?????? ???????????? ???????????????.");
            bindingResult.addError(new FieldError("userPwDTO", "currentPw",
                    e.getMessage()));
            return "user/password";
        }

        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "???????????? ?????? ?????????????????????.", SwalIcon.SUCCESS));
        return "redirect:/user/myInfo";
    }

    @GetMapping("/reserve")
    public String userReserveList(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                                  @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Long userId = principalDetails.getUser().getId();
        pageRequestDTO.setKeyword(userId.toString());

        PageResultDTO<ReserveListDTO, Reserve> reserveList = reserveService.getReserveList(pageRequestDTO);

        model.addAttribute("result", reserveList);
        return "user/reserve";
    }

    @PostMapping
    public String userJoin(@Validated UserDTO userDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (!userDTO.getPassword().equals(userDTO.getCheckPassword())) {
            bindingResult.addError(new FieldError("userDTO", "checkPassword",
                    "???????????? ?????? ?????? ?????????????????????."));
        }
        if (bindingResult.hasErrors()) {
            log.debug("???????????? validation errors = {}", bindingResult);
            return "user/join";
        }

        log.info("???????????? ?????? ??????");
        try {
            userService.userJoin(userDTO);
        } catch (DuplicateUserException e) {
            bindingResult.addError(new FieldError("userDTO", "username", userDTO.getUsername(),
                    false, null, null, "????????? ??????????????? ?????????."));
            return "user/join";
        }

        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("???????????? ??????", "????????? ??? ??????????????????.", SwalIcon.SUCCESS));

        return "redirect:/user/login";
    }
    @GetMapping("/myInfo")
    public String userInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Long userId = principalDetails.getUser().getId();
        UserInfoDTO userInfoDTO = userService.getUserInfo(userId);
        model.addAttribute("userInfoDTO", userInfoDTO);

        return "user/info";
    }

    @GetMapping("/unregister")
    public String userUnregisterForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        model.addAttribute("userId", principalDetails.getUser().getId());
        return "user/unregister";
    }

    @PostMapping("/unregister")
    public String userUnregister(@AuthenticationPrincipal PrincipalDetails principalDetails, Long userId,
                                 RedirectAttributes redirectAttributes) {

        if (principalDetails.getUser().getId() != userId) {
            redirectAttributes.addFlashAttribute("swal",
                    new SwalMessage("Fail", "???????????? ?????? ???????????????.", SwalIcon.ERROR));
            return "redirect:/user/myInfo";
        }

        userService.unregister(userId);
        return "redirect:/logout";
    }
}
