//package com.fpt.swp391.group6.DigitalTome.quartzJob;
//
//import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
//import com.fpt.swp391.group6.DigitalTome.entity.JobExecutionEntity;
//import com.fpt.swp391.group6.DigitalTome.repository.BookRepository;
//import com.fpt.swp391.group6.DigitalTome.repository.JobExecutionRepository;
//import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
//import com.fpt.swp391.group6.DigitalTome.service.EmailService;
//import com.fpt.swp391.group6.DigitalTome.service.JobService;
//import com.fpt.swp391.group6.DigitalTome.service.UserService;
//import jakarta.mail.MessagingException;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//
//@Component
//public class NewBookNotificationJob implements Job {
//
//    private BookRepository bookRepository;
//    private EmailService emailService;
//    private JobExecutionRepository jobExecutionRepository;
//    private UserRepository userRepository;
//    private JobService jobService;
//
//    @Autowired
//    public NewBookNotificationJob(BookRepository bookRepository, EmailService emailService, JobExecutionRepository jobExecutionRepository, JobService jobService, UserRepository userRepository) {
//        this.bookRepository = bookRepository;
//        this.emailService = emailService;
//        this.jobExecutionRepository = jobExecutionRepository;
//        this.jobService = jobService;
//        this.userRepository = userRepository;
//    }
//
//
//
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
//        Date lastExecutionTime = jobService.getLastExecutionTime(jobName);
//
//        System.out.println("Executing job: " + jobName);
//        System.out.println("Last execution time: " + lastExecutionTime);
//
//        List<BookEntity> newBook = bookRepository.findByCreatedDateAfter(lastExecutionTime);
//        if(!newBook.isEmpty()){
//            userRepository.findAll()
//                    .forEach(user -> {
//                        try {
//                            emailService.sendNewBookNotification(user.getEmail(), newBook);
//                        } catch (MessagingException e) {
//                            throw new RuntimeException(e);
//                        }
//                    });
//            jobService.updateLastExecutionTime(jobName);
//        }
//    }
//}
