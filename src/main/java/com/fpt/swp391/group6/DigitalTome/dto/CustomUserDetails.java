package com.fpt.swp391.group6.DigitalTome.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// Đại diện cho thông tin cần thiết của người dùng

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String email;
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Collection<? extends GrantedAuthority> authorities, String username, String email, String password) {
        this.authorities = authorities;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
