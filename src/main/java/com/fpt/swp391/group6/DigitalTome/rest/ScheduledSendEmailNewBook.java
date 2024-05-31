package com.fpt.swp391.group6.DigitalTome.rest;


import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.repository.BookRepository;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import com.fpt.swp391.group6.DigitalTome.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ScheduledSendEmailNewBook {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BookRepository bookRepository;

    @Scheduled(fixedRate = 20000)
//    @Scheduled(cron = "0 0 12 * * ?")
    public void sendNewBook(){

        List<BookEntity> bookEntityList = bookRepository.findBooksCreatedToday();
        if(bookEntityList == null || bookEntityList.isEmpty()){
            System.out.println("There are no new books");
            return;
        }
        List<BookEntity> newBooks = bookEntityList.stream().filter(a -> a.getIsNew() == 0).toList();
        if(newBooks == null || newBooks.isEmpty()){
            System.out.println("No new Books to notify");
            return;
        }
        String newBookNames = newBooks.stream().map(BookEntity::getTitle)
                .collect(Collectors.joining(", "));

        List<String> listEmail = userRepository.findAllGmailNotification(1);
        if(listEmail == null || listEmail.isEmpty()){
            System.out.println("No emails to notify");
            return;
        }
        try {

            emailService.sendNewBookNotification(listEmail, newBookNames);
            System.out.println("Send Email Success");
            
            newBooks.forEach(book -> {
                book.setIsNew(1);  //  đã gửi email, lần sau không gửi lại nữa
                bookRepository.save(book);
            });
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Send Email Fail");
        }

    }
}
