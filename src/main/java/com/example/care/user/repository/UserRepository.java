package com.example.care.user.repository;

import com.example.care.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    User findByUsername(String username);
}
