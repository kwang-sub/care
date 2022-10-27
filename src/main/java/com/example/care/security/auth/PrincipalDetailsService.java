package com.example.care.security.auth;

import com.example.care.user.dto.UserDTO;
import com.example.care.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//시큐리티 설정에서 loginProcessingUrl("/login"); 설정시 해당 요청시 아래 메서드 실행됨
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userService.userSearch(username);
        if (user != null) {
            return new PrincipalDetails(user);
        }
        return null;
    }
}
