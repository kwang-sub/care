package com.example.care.user.controller;

import com.example.care.user.dto.UserDTO;
import com.example.care.user.service.UserService;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import com.example.care.util.exception.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/login")
    public String userLoginForm(@ModelAttribute("userDTO") UserDTO userDTO, HttpSession session) {
        if (session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            return "redirect:/";
        }

        return "/user/loginForm";
    }

    @GetMapping("/user/join")
    public String userJoinForm(@ModelAttribute("userDTO") UserDTO userDTO, HttpSession session) {
        if (session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            return "redirect:/";
        }

        return "/user/joinForm";
    }

    @PostMapping("/user")
    public String userJoin(@Validated UserDTO userDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (!userDTO.getPassword().equals(userDTO.getCheckPassword())) {
            bindingResult.addError(new FieldError("userDTO", "checkPassword",
                    "비밀번호 중복 체크 실패하였습니다."));
        }
        if (bindingResult.hasErrors()) {
            log.debug("회원가입 validation errors = {}", bindingResult);
            return "/user/joinForm";
        }

        log.info("회원가입 로직 시작");
        try {
            userService.userJoin(userDTO);
        } catch (DuplicateUserException e) {
            bindingResult.addError(new FieldError("userDTO", "username", userDTO.getUsername(),
                    false,null, null, "중복된 회원아이디 입니다."));
            return "/user/joinForm";
        }

        redirectAttributes.addFlashAttribute("swal",
                new SwalMessage("회원가입 성공", "로그인 후 이용해주세요.", SwalIcon.SUCCESS));

        return "redirect:/user/login";
    }
}
