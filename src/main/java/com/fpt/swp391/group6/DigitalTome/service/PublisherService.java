package com.fpt.swp391.group6.DigitalTome.service;


import com.fpt.swp391.group6.DigitalTome.dto.PublisherDTO;
<<<<<<< HEAD
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PublisherEntity;
import com.fpt.swp391.group6.DigitalTome.repository.PublisherRepository;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
=======
import com.fpt.swp391.group6.DigitalTome.dto.RegisterPublisherDTO;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.RoleEntity;
import com.fpt.swp391.group6.DigitalTome.repository.RoleRepository;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
>>>>>>> origin/khanhduc-workspace

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

<<<<<<< HEAD
    private final PublisherRepository publisherRepository;

    private final NotificationService notificationService;

    private final UserRepository userRepository;

    private final UserService userService;

    @Autowired
    public PublisherService(NotificationService notificationService, PublisherRepository publisherRepository, UserRepository userRepository, UserService userService) {
        this.notificationService = notificationService;
        this.publisherRepository = publisherRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }



    public List<PublisherDTO> getAllPublisherDetails() {
        List<PublisherEntity> publisherEntities = publisherRepository.findAllWithUserEager();
        List<PublisherDTO> publisherDTO = new ArrayList<>();

        for (PublisherEntity entity : publisherEntities) {
            PublisherDTO dto = new PublisherDTO();
            dto.setId(entity.getId());
            dto.setUserId(entity.getUser().getId());
            dto.setEmail(entity.getUser().getEmail());
            dto.setUsername(entity.getUser().getUsername());
            dto.setNamePublisher(entity.getNamePublisher());
            dto.setCertificateNumber(entity.getCertificateNumber());
            dto.setApproved(entity.isApproved());
            publisherDTO.add(dto);
        }
        return publisherDTO;
    }


    // Lưu tạm thời
    public void savePublisherRequest(PublisherEntity publisher){
        Optional<AccountEntity> accountOptional = userRepository.findById(publisher.getUser().getId());

        if(accountOptional.isPresent()){
            AccountEntity account = accountOptional.get();
            publisher.setApproved(false);
            publisherRepository.save(publisher);

            // Tạo thông báo
            String message = account.getUsername() + " Submitted a registration";
            notificationService.updateNotification(account.getId(), message, "Register", "/register-publisher-details");
        }
    }


    // Lưu khi được duyệt thành công
    public void savePublishers(Long id) {
        System.out.println("Attempting to save publisher with ID: " + id);
        Optional<PublisherEntity> entityOptional = publisherRepository.findById(id);

        if (entityOptional.isPresent()) {
            PublisherEntity publisher = entityOptional.get();
            publisher.setApproved(true);
            publisherRepository.save(publisher);

            userService.updateUserRole(publisher.getUser().getId(), "ROLE_PUBLISHER");
        } else {
            System.out.println("No publisher found with ID: " + id);
        }
    }

    public boolean existsByUserId(Long id){
        return publisherRepository.existsByUserId(id);
    }

    // Xóa khi bị từ chối
    public void removePublisherRequests(Long requestId) {
        Optional<PublisherEntity> entityOptional = publisherRepository.findById(requestId);

        if(entityOptional.isPresent()){
            publisherRepository.deleteById(requestId);
        }

    }
}
=======

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final RoleRepository roleRepository;




    @Autowired
    public PublisherService(NotificationService notificationService, UserRepository userRepository, UserService userService, EmailService emailService, RoleRepository roleRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
        this.roleRepository = roleRepository;

    }

    public List<PublisherDTO> getAllPublisherDetails(){
        List<AccountEntity> accountEntityList = userRepository.findAllByNamePublisherNotNull();

        List<PublisherDTO> publisherDTOList = new ArrayList<>();
        for (AccountEntity accountEntity : accountEntityList){
            PublisherDTO publisherDTO = new PublisherDTO();
            publisherDTO.setUserId(accountEntity.getId());
            publisherDTO.setUsername(accountEntity.getUsername());
            publisherDTO.setNamePublisher(accountEntity.getNamePublisher());
            publisherDTO.setApproved(accountEntity.getIsApproved());
            publisherDTO.setCertificateNumber(accountEntity.getCertificateNumber());
            publisherDTO.setEmail(accountEntity.getEmail());

            publisherDTOList.add(publisherDTO);
        }
        return publisherDTOList;
    }

    public void savePublishers(Long id) {
        AccountEntity accountAdmin = userService.getCurrentLogin();
        Optional<AccountEntity> entityOptional = userRepository.findById(id);
        if (entityOptional.isPresent()) {
            AccountEntity account = entityOptional.get();
            RoleEntity role = roleRepository.findByName("ROLE_PUBLISHER");
            RoleEntity roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            if (role != null && roleAdmin!=null) {
                account.setRoleEntity(role);
                account.setIsApproved(true);
                userRepository.save(account);
                String message = "Publisher Registration Successful";
                notificationService.createNotification(account, accountAdmin, message, "Register", "/logout");
            }
        }
    }

    public void removePublisherRequests(Long id) {
        AccountEntity accountAdmin = userService.getCurrentLogin();

        Optional<AccountEntity> entityOptional = userRepository.findById(id);
        if (entityOptional.isPresent()) {
            AccountEntity account = entityOptional.get();
            RoleEntity role = roleRepository.findByName("ROLE_USER");
            if (role != null) {
                account.setRoleEntity(role);
                account.setIsApproved(null);
                account.setNamePublisher(null);
                account.setCertificateNumber(null);
                userRepository.save(account);
                String message = "Publisher Registration Failed";
                notificationService.createNotification(account, accountAdmin, message, "Register", "/");
            }
        }
    }
    public void contact(String email) {
        AccountEntity accountUser = userService.findByEmail(email);

        if (accountUser != null) {
            RoleEntity roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            if (roleAdmin != null) {
                List<AccountEntity> adminAccounts = userRepository.findAllByRoleEntity(roleAdmin);
                if (!adminAccounts.isEmpty()) {
                    for (AccountEntity adminAccount : adminAccounts) {
                        String messageWithSender = "There is an account opening requirement "+email;
                        notificationService.createNotification(adminAccount, accountUser, messageWithSender, email, "/user-manager");
                    }
                }
            }
        }
    }

    public String registerPublisher(RegisterPublisherDTO registerPublisherDTO, RedirectAttributes redirectAttributes) {
        AccountEntity accountCurrent = userService.getCurrentLogin();
        if (accountCurrent != null) {
            if (accountCurrent.getNamePublisher()!=null) {
                redirectAttributes.addFlashAttribute("error", "You have already submitted a registration request. Please wait for admin approval.");
                return "redirect:/register-publisher";
            }

            accountCurrent.setCertificateNumber(registerPublisherDTO.getCertificateNumber());
            accountCurrent.setNamePublisher(registerPublisherDTO.getNamePublisher());
            accountCurrent.setIsApproved(false);
            userRepository.save(accountCurrent);


            String subject = "Publisher Registration Confirmation";
            String htmlContent = "Dear " + accountCurrent.getUsername() + ",<br/>Your request to become a publisher has been received. We will review your request shortly.";
            try {
                emailService.sendEmail(subject, htmlContent, List.of(accountCurrent.getEmail()));
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            String message = accountCurrent.getUsername() + " Submitted a registration";
            List<AccountEntity>accountAdminEntityList =userRepository.findByRoleName("ROLE_ADMIN");
            for (AccountEntity accountEntity : accountAdminEntityList){
                notificationService.createNotification(accountEntity,accountCurrent, message, "Register", "/register-publisher-details");
            }
        }
        redirectAttributes.addFlashAttribute("success", "You have successfully registered, please give us your feedback");
        return "redirect:/register-publisher";
    }
}
>>>>>>> origin/khanhduc-workspace
