//package com.fpt.swp391.group6.DigitalTome.quartzJob;
//
//
//import org.quartz.CronScheduleBuilder;
//import org.quartz.JobDetail;
//import org.quartz.Trigger;
//import org.quartz.TriggerBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TriggerConfig {
//
//    @Bean(name = "jobTrigger")
//    public Trigger jobTrigger(JobDetail jobDetail) {
//        System.out.println("Initial execute triggerRunboot ");
//        try {
//            String time = "0/10 * * * * ?";
//            return TriggerBuilder.newTrigger()
//                    .forJob(jobDetail) // Liên kết Trigger với JobDetail
//                    .withIdentity("RUN_QUARTZ", "JOB_GROUP")
//                    .startNow()
//                    .withSchedule(CronScheduleBuilder.cronSchedule(time))
//                    .build();
//        } catch (Exception e) {
//            System.out.println("Error trigger " + e);
//            return null;
//        }
//    }
//}
//
