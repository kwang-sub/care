package com.example.care.user.service;

import com.example.care.user.dto.UserDTO;
import com.example.care.user.dto.UserInfoDTO;

public interface UserService {

    UserDTO userJoin(UserDTO userDto);

    UserDTO userSearch(String name);

    UserInfoDTO getUserInfo(Long userId);
}
