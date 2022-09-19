package com.example.care.user.service;

import com.example.care.user.domain.Role;
import com.example.care.user.domain.User;
import com.example.care.user.dto.UserDTO;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.exception.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public UserDTO userJoin(UserDTO userDto) {
        User findUser = userRepository.findByUsername(userDto.getUsername());
        if (findUser != null) {
            throw new DuplicateUserException("중복 회원입니다.");
        }
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User user = userJoinDTOToEntity(userDto);
        userRepository.save(user);
        return userDto;
    }

    @Override
    public UserDTO userSearch(String username) {
        User user = userRepository.findByUsername(username);
        return userEntityToJoin2DTO(user);
    }

    private User userJoinDTOToEntity(UserDTO userDTO) {
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

    private UserDTO userEntityToJoin2DTO(User userEntity) {
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
