package com.readbook.ReadBook.config;

import com.readbook.ReadBook.entity.AccountEntity;
import com.readbook.ReadBook.entity.RoleEntity;
import com.readbook.ReadBook.repository.RoleRepository;
import com.readbook.ReadBook.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService( UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        AccountEntity user = userRepository.findByEmail(email);
        if (user == null) {
            user = new AccountEntity();
            user.setEmail(email);
            user.setUsername(oAuth2User.getAttribute("name"));

            String randomPassword = "123";
            String encodedPassword = passwordEncoder.encode(randomPassword);
            user.setPassword(encodedPassword);

            RoleEntity roleUser = roleRepository.findByName("ROLE_USER");
            if (roleUser == null) {
                throw new RuntimeException("Role 'ROLE_USER' not found in the database");
            }
            user.setRoleEntity(roleUser);
            userRepository.save(user);
        }

        else {
            user.setUsername(oAuth2User.getAttribute("name"));
            userRepository.save(user);
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new CustomOAuth2User(oAuth2User, authorities);
    }
}



