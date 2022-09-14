package com.example.care.user.service;

import com.example.care.user.domain.User;
import com.example.care.user.dto.UserJoinDTO;
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
    public UserJoinDTO userJoin(UserJoinDTO userJoinDto) {
        Optional<User> findUser = userRepository.findByUsername(userJoinDto.getUsername());
        if (!findUser.isEmpty()) {
            throw new DuplicateUserException("중복 회원입니다.");
        }
        userJoinDto.setPassword(bCryptPasswordEncoder.encode(userJoinDto.getPassword()));
        User user = userJoinDTOToEntity(userJoinDto);
        userRepository.save(user);
        return userJoinDto;
    }

    @Override
    public UserJoinDTO findUserByName(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return userEntityToJoin2DTO(user);
    }
}
