package com.example.care.config;

import com.example.care.security.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록됨
@RequiredArgsConstructor
public class SecurityConfig  {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final BCryptPasswordEncoder encodePwd;

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();


        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/");

        http.authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/myInfo", "/user/reserve").access( "hasRole('ROLE_USER')")
                .antMatchers("/reserve/**").access( "hasRole('ROLE_USER')")
                .antMatchers("/board/register").access( "hasRole('ROLE_USER')")
                .antMatchers("/board/**/modify").access( "hasRole('ROLE_USER')")
                .antMatchers("/pay/**").access("hasRole('ROLE_USER')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/user/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/user/login")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

        return http.build();
    }

    /**
     * 로그아웃 후 동일 사용자 재접속 세션문제 해결하는 부분
     * https://wildeveloperetrain.tistory.com/165 참고
     */
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    /**
     * 인가 처리 부분 사용자 접근권한에는 관리자도 접근가능하게 설정
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);

        return roleHierarchy;
    }
}
