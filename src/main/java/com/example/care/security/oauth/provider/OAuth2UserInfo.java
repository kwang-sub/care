package com.example.care.security.oauth.provider;

public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
