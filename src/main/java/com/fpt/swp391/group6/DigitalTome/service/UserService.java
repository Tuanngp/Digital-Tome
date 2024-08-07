package com.fpt.swp391.group6.DigitalTome.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.swp391.group6.DigitalTome.dto.RegisterDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.RoleEntity;
import com.fpt.swp391.group6.DigitalTome.enums.Gender;
import com.fpt.swp391.group6.DigitalTome.mapper.UserMapper;
import com.fpt.swp391.group6.DigitalTome.repository.RoleRepository;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import com.fpt.swp391.group6.DigitalTome.utils.UserUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.fpt.swp391.group6.DigitalTome.controller.ProfileController.DEFAULT_AVATAR_URL;

@Service
public class UserService {


    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Cloudinary cloudinary;

    public UserService(Cloudinary cloudinary, UserMapper userMapper, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.cloudinary = cloudinary;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<AccountEntity> fetchAllAccount(){
        return userRepository.findAll();
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

    public  void  updatePoint (AccountEntity accountEntity){
        userRepository.save(accountEntity);
    }

    public AccountEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found with id: " + id));
    }

    public String getEmailById(Long userId) {
        return userRepository.findById(userId)
                .map(AccountEntity::getEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void saveUser(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("User exists");
        }

        AccountEntity user = userMapper.toUSer(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setAvatarPath(DEFAULT_AVATAR_URL);

        RoleEntity role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoleEntity(role);
        user.setAvatarPath(DEFAULT_AVATAR_URL);
        userRepository.save(user);
    }

    public void save(AccountEntity account){
        userRepository.save(account);
    }

    private RoleEntity checkRoleExist() {
        RoleEntity role = new RoleEntity();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
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

    public String getImage(String username){
        AccountEntity account = userRepository.findByUsername(username);
        if (account != null) {
            return account.getAvatarPath();
        }
        return DEFAULT_AVATAR_URL;
    }

    public List<AccountEntity> findAccountsByExpiredMembership(Date currentDate){
        return userRepository.findAccountsByExpiredMembership(currentDate);
    }

    public AccountEntity getCurrentLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if(authentication != null && authentication.getPrincipal() != null){
            username = authentication.getPrincipal().toString();
        }
        if(username != null){
            return userRepository.findByUsername(username);
        }
        return null;
    }

    public AccountEntity registerPublisher(Long id) {
        Optional<AccountEntity> account = userRepository.findById(id);
        AccountEntity user = null;
        if (account.isPresent()) {
            user = account.get();
            user.setRoleEntity(roleRepository.findByName("ROLE_PUBLISHER"));
        }
        assert user != null;
        return userRepository.save(user);
    }

    public AccountEntity getCurrentUser(Principal principal,@AuthenticationPrincipal OAuth2User oAuth2User) {
        String username = null;
        if (principal != null) {
            username = principal.getName();
        } else if (oAuth2User != null) {
            username = oAuth2User.getAttribute("email");
        }
        return username != null ? findByUsername(username) : null;
    }

    public AccountEntity search(String keyword) {

        if (keyword.contains("@")) {
            return userRepository.findByEmail(keyword);
        }
        if (keyword.matches("\\d{8,11}")) {
            return userRepository.findByPhone(keyword);
        }
        return userRepository.findByUsername(keyword);
    }
    public List<AccountEntity> getAdminUsers() {
        return userRepository.findByRoleName("ADMIN");
    }

    public BigDecimal getPercentageByGender(Gender gender){
        long totalUsers = userRepository.countTotalUsers(); // Lấy tổng số người dùng
        if(totalUsers == 0){
            return BigDecimal.ZERO;
        }

        long genderCount = userRepository.countByGender(gender); // Lấy số lượng người dùng theo giới tính

        return BigDecimal.valueOf(genderCount).divide(BigDecimal.valueOf(totalUsers), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getMalePercentage() {
        return getPercentageByGender(Gender.MALE);
    }

    public BigDecimal getFemalePercentage() {
        return getPercentageByGender(Gender.FEMALE);
    }

    public BigDecimal getOtherPercentage() {
        return getPercentageByGender(Gender.OTHER);
    }
}