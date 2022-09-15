package com.example.care.user.service;

import com.example.care.user.dto.UserDTO;
import com.example.care.util.exception.DuplicateUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    UserDTO userDTO = null;

    @BeforeEach
    void setup() {
        userDTO = UserDTO.builder()
                .username("userId")
                .password("password")
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void userJoinTest() {
        //when
        userService.userJoin(userDTO);
        //then
        UserDTO findUser = userService.findUserByName(userDTO.getUsername());
        assertThat(findUser).isNotNull();
        assertThat(findUser.getUsername()).isEqualTo(userDTO.getUsername());
    }

    @Test
    @DisplayName("일반 사용자 회원가입 권한부여 테스트")
    void userJoinRoleTest() {
        //when
        userService.userJoin(userDTO);
        //then
        UserDTO findUser = userService.findUserByName(userDTO.getUsername());
        assertThat(findUser.getRoleKey()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("회원가입 암호화 테스트")
    void userJoinPasswordTest() {
        //given
        String password = "password";
        UserDTO userDTO = UserDTO.builder()
                .username("userId")
                .password(password)
                .build();
        //when
        userService.userJoin(userDTO);
        //then
        UserDTO findUser = userService.findUserByName(userDTO.getUsername());
        assertThat(bCryptPasswordEncoder.matches(password, findUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("중복 아이디 예외 테스트")
    void userJoinDuplicateUserTest() {
        //given
        UserDTO userDTO1 = UserDTO.builder()
                .username("duplication")
                .password("password")
                .build();
        userService.userJoin(userDTO1);

        //when
        UserDTO userDTO2 = UserDTO.builder()
                .username("duplication")
                .password("password")
                .build();
        //then
        assertThatThrownBy(() -> userService.userJoin(userDTO2))
                .isInstanceOf(DuplicateUserException.class);
    }
}