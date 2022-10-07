package com.example.care.user.repository;

import com.example.care.user.domain.User;

public interface UserRepositoryCustom {

    User findUserInfoByUserId(Long userId);
}
