package com.fpt.swp391.group6.DigitalTome.service;


import com.fpt.swp391.group6.DigitalTome.dto.PublisherDTO;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PublisherEntity;
import com.fpt.swp391.group6.DigitalTome.repository.PublisherRepository;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

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