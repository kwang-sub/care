package com.example.care.user.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

class UserDTOValidationTest {

    @Test
    void 회원가입폼_테스트() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        UserJoinDTO userJoinDTO = UserJoinDTO.builder()
                .username("")
                .nickname("asdf")
                .password("asdf")
                .email("asdf")
                .build();
        Set<ConstraintViolation<UserJoinDTO>> validations = validator.validate(userJoinDTO);
        for (ConstraintViolation<UserJoinDTO> validation : validations) {
            System.out.println(validation);
            System.out.println(validation.getMessage());
        }
    }
}