package com.example.registrationlogin.config;

import com.example.registrationlogin.entity.CustomOAuth2User;
import com.example.registrationlogin.entity.Role;
import com.example.registrationlogin.entity.User;
import com.example.registrationlogin.repository.RoleRepository;
import com.example.registrationlogin.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import static com.example.registrationlogin.service.UserService.*;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(EntityManager entityManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // OAuth2User: cho người dùng đăng nhập vào ứng dụng bằng cách sử dụng thông tin xác thực từ một bên thứ 3 mà không cần tạo tk mk
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(oAuth2User.getAttribute("name"));

            String randomPassword = "123";
            String encodedPassword = passwordEncoder.encode(randomPassword);
            user.setPassword(encodedPassword);

            Role roleUser = roleRepository.findByName("ROLE_USER");
            if (roleUser == null) {
                throw new RuntimeException("Role 'ROLE_USER' not found in the database");
            }
            user.setRoles(List.of(roleUser));
            userRepository.save(user);
        }

        // Cập nhật thông tin người dùng nếu có thay đổi
        else {
            user.setUsername(oAuth2User.getAttribute("name"));
            userRepository.save(user);
        }

        // Tạo authorities
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomOAuth2User(oAuth2User, authorities);
    }
}