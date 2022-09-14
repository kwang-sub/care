package com.example.care.user.service;

import com.example.care.user.domain.Role;
import com.example.care.user.domain.User;
import com.example.care.user.dto.UserJoinDTO;

public interface UserService {


    UserJoinDTO userJoin(UserJoinDTO userJoinDto);

    UserJoinDTO findUserByName(String name);

    default User userJoinDTOToEntity(UserJoinDTO userJoinDTO) {
        User user = User.builder()
                .username(userJoinDTO.getUsername())
                .nickname(userJoinDTO.getNickname())
                .password(userJoinDTO.getPassword())
                .email(userJoinDTO.getEmail())
                .role(Role.USER)
                .provider(userJoinDTO.getProvider())
                .build();
        return user;
    }

    default UserJoinDTO userEntityToJoin2DTO(User userEntity) {
        if (userEntity != null) {
            UserJoinDTO userJoinDTO = UserJoinDTO.builder()
                    .username(userEntity.getUsername())
                    .password(userEntity.getPassword())
                    .nickname(userEntity.getNickname())
                    .email(userEntity.getEmail())
                    .role(userEntity.getRole())
                    .provider(userEntity.getProvider())
                    .build();
            return userJoinDTO;
        }
        return null;
    }

}
