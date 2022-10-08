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
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
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
import java.util.function.Function;

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

        return "/user/login";
    }

    @GetMapping("/join")
    public String userJoinForm(@ModelAttribute("userDTO") UserDTO userDTO, HttpSession session) {
        if (session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            return "redirect:/";
        }

        return "/user/join";
    }

    @GetMapping("/profile")
    public String userProfileModifyForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Long userId = principalDetails.getUser().getId();
        model.addAttribute("userProfileDTO", userService.getUserProfile(userId));
        return "/user/profile";
    }

    @PostMapping("/{userId}/profile")
    public String userProfileModify(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("userId") Long userId,
                                    @Validated UserProfileDTO userProfileDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("비밀번호 변경 validation errors = {}", bindingResult);
            return "/user/profile";
        }
        if (principalDetails.getUser().getId() != userId) {
            log.error("로그인 계정아닌 다른 계정 정보수정 요청");
            redirectAttributes.addFlashAttribute("swal",
                    new SwalMessage("Error", "잘못된 요청입니다.", SwalIcon.ERROR));
            return "redirect:/user/profile";
        }
        userService.profileModify(userProfileDTO);
        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "회원정보 수정 완료되었습니다.", SwalIcon.SUCCESS));

        return "redirect:/user/profile";
    }

    @GetMapping("/password")
    public String userPwModifyForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Long userId = principalDetails.getUser().getId();
        UserPwDTO userPwDTO = new UserPwDTO();
        userPwDTO.setUserId(userId);
        model.addAttribute("userPwDTO", userPwDTO);
        return "/user/password";
    }

    @PostMapping("/{userId}/password")
    public String userPasswordModify(@PathVariable("userId")Long userId, @AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @Validated UserPwDTO userPwDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Long loginUserId = principalDetails.getUser().getId();

        if (!userPwDTO.getModPw().equals(userPwDTO.getCheckModPw())) {
            bindingResult.addError(new FieldError("userPwDTO", "checkModPw",
                    "비밀번호 중복 체크 실패하였습니다."));
        }
        if (bindingResult.hasErrors()) {
            log.info("비밀번호 변경 validation errors = {}", bindingResult);
            return "/user/password";
        }
        if (loginUserId != userId) {
            log.info("로그인계정 아닌 다른 계정 비밀번호 변경요청");
            redirectAttributes.addFlashAttribute("swal",
                    new SwalMessage("error", "잘못된 요청입니다.", SwalIcon.ERROR));
            return "redirect:/user/password";
        }

        try {
            userService.passwordModify(userPwDTO);
        } catch (UserAccessException e) {
            log.error("잘못된 현재 비밀번호 요청입니다.");
            bindingResult.addError(new FieldError("userPwDTO", "currentPw",
                    e.getMessage()));
            return "/user/password";
        }

        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("Success", "비밀번호 변경 성공하였습니다.", SwalIcon.SUCCESS));
        return "redirect:/user/myInfo";
    }

    @GetMapping("/reserve")
    public String userReserveList(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                                  @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Long userId = principalDetails.getUser().getId();
        pageRequestDTO.setKeyword(userId.toString());

        PageResultDTO<ReserveListDTO, Reserve> reserveList = reserveService.getReserveList(pageRequestDTO);

        model.addAttribute("result", reserveList);
        return "/user/reserve";
    }

    @PostMapping
    public String userJoin(@Validated UserDTO userDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (!userDTO.getPassword().equals(userDTO.getCheckPassword())) {
            bindingResult.addError(new FieldError("userDTO", "checkPassword",
                    "비밀번호 중복 체크 실패하였습니다."));
        }
        if (bindingResult.hasErrors()) {
            log.debug("회원가입 validation errors = {}", bindingResult);
            return "/user/join";
        }

        log.info("회원가입 로직 시작");
        try {
            userService.userJoin(userDTO);
        } catch (DuplicateUserException e) {
            bindingResult.addError(new FieldError("userDTO", "username", userDTO.getUsername(),
                    false, null, null, "중복된 회원아이디 입니다."));
            return "/user/join";
        }

        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("회원가입 성공", "로그인 후 이용해주세요.", SwalIcon.SUCCESS));

        return "redirect:/user/login";
    }

    @GetMapping("/myInfo")
    public String userInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Long userId = principalDetails.getUser().getId();
        System.out.println(userId);
        UserInfoDTO userInfoDTO = userService.getUserInfo(userId);
        model.addAttribute("userInfoDTO", userInfoDTO);

        return "/user/info";
    }
}
