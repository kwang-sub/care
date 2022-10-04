package com.example.care.user.service;

import com.example.care.membership.domain.MembershipHistory;
import com.example.care.product.domain.MembershipProduct;
import com.example.care.product.domain.ProductCode;
import com.example.care.user.domain.Role;
import com.example.care.user.domain.User;
import com.example.care.user.dto.UserDTO;
import com.example.care.user.dto.UserInfoDTO;
import com.example.care.user.repository.UserRepository;
import com.example.care.util.ex.exception.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        User user = userDTOToEntity(userDto);
        userRepository.save(user);
        userDto.setId(userDto.getId());
        return userDto;
    }

    @Override
    public UserDTO userSearch(String username) {
        User user = userRepository.findByUsername(username);
        return userEntityToDTO(user);
    }

    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        User user = userRepository.findUserInfoByUserId(userId);
        MembershipHistory membershipHistory = null;
        UserInfoDTO userInfoDTO = null;
        if (!user.getMembershipHistoryList().isEmpty()) {
            membershipHistory = user.getMembershipHistoryList().get(0);
            List<MembershipProduct> membershipProductList = membershipHistory.getMembership().getMembershipProductList();

            AtomicInteger cleanMaxNum = new AtomicInteger();
            AtomicInteger counselMaxNum = new AtomicInteger();
            AtomicInteger transportMaxNum = new AtomicInteger();
            membershipProductList.stream()
                    .forEach(membershipProduct -> {
                        if (membershipProduct.getProduct().getCode().equals(ProductCode.COUNSEL)) {
                            counselMaxNum.set(membershipProduct.getMaxNum());
                        } else if (membershipProduct.getProduct().getCode().equals(ProductCode.CLEAN)) {
                            cleanMaxNum.set(membershipProduct.getMaxNum());
                        } else if (membershipProduct.getProduct().getCode().equals(ProductCode.TRANSPORT)) {
                            transportMaxNum.set(membershipProduct.getMaxNum());
                        }

                    });

            userInfoDTO = UserInfoDTO.builder()
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .userRegDate(user.getRegDate())
                    .provider(user.getProvider())
                    .grade(membershipHistory.getMembership().getGrade())
                    .membershipRegDate(membershipHistory.getRegDate())
                    .membershipEndDate(membershipHistory.getEndDate())
                    .cleanMaxNum(cleanMaxNum.get())
                    .counselMaxNum(counselMaxNum.get())
                    .transportMaxNum(transportMaxNum.get())
                    .cleanUseNum(membershipHistory.getCleanUseNum())
                    .counselUseNum(membershipHistory.getCounselUseNum())
                    .transportUseNum(membershipHistory.getTransportUseNum())
                    .build();
        }
        System.out.println(userInfoDTO);
        return userInfoDTO;
    }

    private User userDTOToEntity(UserDTO userDTO) {
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

    private UserDTO userEntityToDTO(User userEntity) {
        if (userEntity != null) {
            UserDTO userDTO = UserDTO.builder()
                    .id(userEntity.getId())
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
