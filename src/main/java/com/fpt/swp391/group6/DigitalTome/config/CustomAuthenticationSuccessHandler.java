package com.fpt.swp391.group6.DigitalTome.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        List<String> roles = new ArrayList<>();
        boolean isAdmin = false;

        for(GrantedAuthority grantedAuthority : authentication.getAuthorities()){
            roles.add(grantedAuthority.getAuthority());
            if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                isAdmin = true;
                break;
            }
        }
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("role", roles);
        for(String role : roles){
            System.out.println("Roles đã đăng nhập là: "+role);
        }
        if(isAdmin){
            response.sendRedirect("/admin");
        }else{
            response.sendRedirect("/index");
        }
    }
}
