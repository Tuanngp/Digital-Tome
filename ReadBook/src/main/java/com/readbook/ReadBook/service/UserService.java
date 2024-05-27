package com.readbook.ReadBook.service;

import com.readbook.ReadBook.dto.UserDto;
import com.readbook.ReadBook.entity.AccountEntity;
import com.readbook.ReadBook.entity.RoleEntity;
import com.readbook.ReadBook.mapper.UserMapper;
import com.readbook.ReadBook.repository.RoleRepository;
import com.readbook.ReadBook.repository.UserRepository;
import com.readbook.ReadBook.utils.UserUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void saveUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("User exists");
        }

        AccountEntity user = userMapper.toUSer(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        RoleEntity role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoleEntity(role);
        userRepository.save(user);
    }

    private RoleEntity checkRoleExist() {
        RoleEntity role = new RoleEntity();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public AccountEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public AccountEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserDto> findAllUsers() {
        List<AccountEntity> users = userRepository.findAll();
        return userMapper.toUserDto(users);
    }

    public String forgotPass(String email) {
        Optional<AccountEntity> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userOptional.isPresent()) {
            return "Invalid email id.";
        }
        AccountEntity user = userOptional.get();
        user.setToken(UserUtils.generateToken());
        user.setTokenCreationDate(LocalDateTime.now());
        user = userRepository.save(user);
        return user.getToken();
    }

    public String resetPass(String token, String password) {
        Optional<AccountEntity> userOptional = Optional.ofNullable(userRepository.findByToken(token));
        if (!userOptional.isPresent()) {
            return "Invalid token";
        }
        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();


        if (UserUtils.isTokenExpired(tokenCreationDate)) {
            return "Token expired.";
        }

        AccountEntity user = userOptional.get();

        user.setPassword(passwordEncoder.encode(password));
        user.setToken(null);
        user.setTokenCreationData(null);
        userRepository.save(user);
        return "Your password successfully updated.";
    }


    public void updateImage(String url, String username) {
        AccountEntity user = userRepository.findByUsername(username);
        user.setAvatarPath(url);
        userRepository.save(user);
    }
}

