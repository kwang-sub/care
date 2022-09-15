package com.example.care.user.service;

import com.example.care.user.domain.Role;
import com.example.care.user.domain.User;
import com.example.care.user.dto.UserDTO;

public interface UserService {


    UserDTO userJoin(UserDTO userDto);

    UserDTO findUserByName(String name);

    default User userJoinDTOToEntity(UserDTO userDTO) {
        User user = User.builder()
                .username(userDTO.getUsername())
                .nickname(userDTO.getNickname())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .role(Role.USER)
                .provider(userDTO.getProvider())
                .build();
        return user;
    }

    default UserDTO userEntityToJoin2DTO(User userEntity) {
        if (userEntity != null) {
            UserDTO userDTO = UserDTO.builder()
                    .username(userEntity.getUsername())
                    .password(userEntity.getPassword())
                    .nickname(userEntity.getNickname())
                    .email(userEntity.getEmail())
                    .role(userEntity.getRole())
                    .provider(userEntity.getProvider())
                    .build();
            return userDTO;
        }
        return null;
    }

}
