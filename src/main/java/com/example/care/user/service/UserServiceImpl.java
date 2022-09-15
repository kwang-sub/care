package com.example.care.user.service;

import com.example.care.user.domain.User;
import com.example.care.user.dto.UserDTO;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.exception.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public UserDTO userJoin(UserDTO userDto) {
        Optional<User> findUser = userRepository.findByUsername(userDto.getUsername());
        if (!findUser.isEmpty()) {
            throw new DuplicateUserException("중복 회원입니다.");
        }
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User user = userJoinDTOToEntity(userDto);
        userRepository.save(user);
        return userDto;
    }

    @Override
    public UserDTO findUserByName(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return userEntityToJoin2DTO(user);
    }
}
