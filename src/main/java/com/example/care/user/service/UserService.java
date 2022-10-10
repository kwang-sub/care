package com.example.care.user.service;

import com.example.care.user.dto.UserDTO;
import com.example.care.user.dto.UserInfoDTO;
import com.example.care.user.dto.UserProfileDTO;
import com.example.care.user.dto.UserPwDTO;

public interface UserService {

    UserDTO userJoin(UserDTO userDto);

    UserDTO userSearch(String name);

    UserInfoDTO getUserInfo(Long userId);

    void passwordModify(UserPwDTO userPwDTO);

    UserProfileDTO getUserProfile(Long userId);

    void profileModify(UserProfileDTO userProfileDTO);


}
