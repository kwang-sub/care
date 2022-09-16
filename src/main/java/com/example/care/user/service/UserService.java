package com.example.care.user.service;

import com.example.care.user.dto.UserDTO;

public interface UserService {

    UserDTO userJoin(UserDTO userDto);

    UserDTO userSearch(String name);
}
