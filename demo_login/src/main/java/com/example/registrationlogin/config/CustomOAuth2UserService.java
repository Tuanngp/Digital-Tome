package com.example.registrationlogin.config;

import com.example.registrationlogin.entity.CustomOAuth2User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    // OAuth2User: cho người dùng đăng nhập vào ứng dụng bằng cách sử dụng thông tin xác thực từ một bên thứ 3 mà không cần tạo tk mk
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Set<GrantedAuthority> authorities = new HashSet<>();  // GrantedAuthority: kiểm soát quyền truy cập
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomOAuth2User(oAuth2User, authorities);
    }
}
