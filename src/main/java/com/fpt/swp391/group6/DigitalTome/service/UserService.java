package com.fpt.swp391.group6.DigitalTome.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.swp391.group6.DigitalTome.dto.RegisterDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.RoleEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.UserMapper;
import com.fpt.swp391.group6.DigitalTome.repository.RoleRepository;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import com.fpt.swp391.group6.DigitalTome.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public AccountEntity findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void saveUser(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("User exists");
        }

        AccountEntity user = userMapper.toUSer(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        RoleEntity role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoleEntity(role);
        userRepository.save(user);
    }

    private RoleEntity checkRoleExist() {
        RoleEntity role = new RoleEntity();
        role.setName("ROLE_USER");
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

    public AccountEntity findByUsername(String username){
        return userRepository.findByUsername(username);
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
        user.setTokenCreationDate(null);
        userRepository.save(user);
        return "Your password successfully updated.";
    }

    // Nháº­n tá»‡p tin tá»« ngÆ°á»i dÃ¹ng vÃ  tráº£ vá» url
    public String uploadImage(MultipartFile file) throws IOException {
        try{
            var result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "/avatar",
                    "use_filename", true,
                    "unique_filename", true,
                    "resource_type","auto"
            ));

            return  result.get("secure_url").toString();
        } catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }



    public Boolean destroyImage(String nameOfImage){
        try {
            var result  = cloudinary.uploader().destroy( nameOfImage, ObjectUtils.asMap(
                    "folder", "/avatar",
                    "resource_type","image"
            ));
        }catch (IOException io){
            throw new RuntimeException("Image destroy fail");
        }
        return true;
    }

    public void updateImage(String url, String username) {
        AccountEntity user = userRepository.findByUsername(username);
        if (user != null) {
            user.setAvatarPath(url);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }



    public  void  updatePoint (AccountEntity accountEntity){
        userRepository.save(accountEntity);
    }
}

