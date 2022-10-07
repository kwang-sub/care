package com.example.care.security.oauth;

import com.example.care.security.auth.PrincipalDetails;
import com.example.care.security.oauth.provider.GoogleUserInfo;
import com.example.care.security.oauth.provider.NaverUserInfo;
import com.example.care.security.oauth.provider.OAuth2UserInfo;
import com.example.care.user.domain.Role;
import com.example.care.user.dto.UserDTO;
import com.example.care.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("OAuth2 회원가입 로직 {}", userRequest.getClientRegistration().getRegistrationId());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo userInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            userInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            userInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        }

        String nickname = userInfo.getName();
        String provider = userInfo.getProvider();
        String providerId = userInfo.getProviderId();
        String email = userInfo.getEmail();
        String password = encoder.encode(UUID.randomUUID().toString());
        String name = provider + "_" + providerId;

        UserDTO user = userService.userSearch(name);

        if (user == null) {
            user = userService.userJoin(UserDTO.builder()
                    .username(name)
                    .nickname(nickname)
                    .password(password)
                    .email(email)
                    .role(Role.USER)
                    .provider(provider)
                    .build());
        }
        httpSession.setAttribute("provider", provider);
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }


}
