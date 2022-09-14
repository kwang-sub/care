package com.example.care.user.controller;

import com.example.care.security.auth.PrincipalDetails;
import com.example.care.user.dto.UserJoinDTO;
import com.example.care.user.service.UserService;
import com.example.care.util.exception.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/login")
    public String userLoginForm(@ModelAttribute("userDTO") UserJoinDTO userDTO) {
        return "/user/loginForm";
    }

    @GetMapping("/user/join")
    public String userJoinForm(@ModelAttribute("userJoinDTO") UserJoinDTO userJoinDTO) {
        return "/user/joinForm";
    }

    @PostMapping("/user")
    public String userJoin(@Validated UserJoinDTO userJoinDTO, BindingResult bindingResult) {
        if (!userJoinDTO.getPassword().equals(userJoinDTO.getCheckPassword())) {
            bindingResult.addError(new FieldError("UserJoinDTO", "checkPassword",
                    "비밀번호 중복 체크 실패하였습니다."));
        }
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "/user/joinForm";
        }

        try {
            userService.userJoin(userJoinDTO);
        } catch (DuplicateUserException e) {
            bindingResult.addError(new FieldError("UserJoinDTO", "username",userJoinDTO.getUsername(),
                    false,null, null, "중복된 회원아이디 입니다."));
            return "/user/joinForm";
        }

        return "redirect:/user/login";
    }

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(
                                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("/test/login=================");
        System.out.println(principalDetails.getUser());

        return "세션 정보 확인하기";
    }
}
