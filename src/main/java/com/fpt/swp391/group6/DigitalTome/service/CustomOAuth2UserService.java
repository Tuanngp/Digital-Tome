package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.config.CustomOAuth2User;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.RoleEntity;
import com.fpt.swp391.group6.DigitalTome.repository.RoleRepository;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import com.fpt.swp391.group6.DigitalTome.service.EmailService;
import com.fpt.swp391.group6.DigitalTome.utils.UserUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private HttpServletRequest request;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public CustomOAuth2UserService(EmailService emailService, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        AccountEntity account = userRepository.findByEmail(email);
        if (account == null) account = createUser(oAuth2User);

//        account.setAvatarPath("../user/images/avatar_default.png");
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        HttpSession session = request.getSession();
        session.setAttribute("user", account);
        session.setAttribute("role", authorities.stream().map(GrantedAuthority::getAuthority).toList());

        return new CustomOAuth2User(oAuth2User, authorities);
    }

    private AccountEntity createUser(OAuth2User oAuth2User) {
        AccountEntity account = new AccountEntity();
        account.setEmail(oAuth2User.getAttribute("email"));
        account.setUsername(oAuth2User.getAttribute("email"));
        account.setFullname(oAuth2User.getAttribute("name"));
        String password = UserUtils.generateToken();

        account.setAvatarPath("../user/images/avatar_default.png");
        String encodePassword = passwordEncoder.encode(password);
        account.setPassword(encodePassword);

        RoleEntity role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = new RoleEntity();
            role.setName("ROLE_USER");
            roleRepository.save(role);
        }
        account.setRoleEntity(role);
        AccountEntity savedAccount = userRepository.save(account);

        try {
            emailService.sendEmail("Your login password " + password,
                    "Your login password is: " + password,
                    Collections.singletonList(savedAccount.getEmail()));
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email to user");
        }
        return savedAccount;
    }
}