//package com.fpt.swp391.group6.DigitalTome.quartzJob;
//
//import org.quartz.JobBuilder;
//import org.quartz.JobDetail;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class JobConfig {
//
//    @Bean(name = "jobDetail")
//    public JobDetail jobDetail(){
//        return JobBuilder.newJob(NewBookNotificationJob.class)
//                .withIdentity("RUN_QUARTZ", "JOB_GROUP")
//                .withDescription("description job")
//                .storeDurably(true)
//                .build();
//    }
//}
//
