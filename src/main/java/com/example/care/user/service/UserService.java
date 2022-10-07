package com.example.care.user.service;

import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveListDTO;
import com.example.care.user.dto.UserDTO;
import com.example.care.user.dto.UserInfoDTO;
import com.example.care.user.dto.UserProfileDTO;
import com.example.care.user.dto.UserPwDTO;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;

import java.util.function.Function;

public interface UserService {

    UserDTO userJoin(UserDTO userDto);

    UserDTO userSearch(String name);

    UserInfoDTO getUserInfo(Long userId);

    void passwordModify(UserPwDTO userPwDTO);

    UserProfileDTO getUserProfile(Long userId);

    void profileModify(UserProfileDTO userProfileDTO);


}
