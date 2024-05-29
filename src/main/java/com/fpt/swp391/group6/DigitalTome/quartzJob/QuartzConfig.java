//package com.fpt.swp391.group6.DigitalTome.quartzJob;
//
//import org.quartz.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
//@Configuration
//public class QuartzConfig  {
//    @Bean
//    public JobDetail newBookNotificationJobDetail(){
//        return JobBuilder.newJob(NewBookNotificationJob.class)
//                .withDescription("newBookNotificationJob")
//                .storeDurably()
//                .build();
//    }
//
//    @Bean
//    public Trigger newBookNotificationJobTrigger(){
////        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
////                .withIntervalInHours(24) // Lên lịch chạy mỗi 24 giờ một lần
////                .repeatForever();
//
////        return TriggerBuilder.newTrigger()
////                .forJob(newBookNotificationJobDetail())
////                .withIdentity("newBookNotificationTrigger")
////                .startAt(time("02/03/2024 21:51:02"))
////                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//
////                .withIntervalInMinutes(5)
////                .repeatForever())
////                .build();
////        kích hoạt sau một khoảng thời gian cố định từ thời điểm hiện tại,
////        => sử dụng withIntervalInSeconds và withRepeatCount
//
//            return TriggerBuilder.newTrigger()
//                    .forJob(newBookNotificationJobDetail())
//                    .withIdentity("newBookNotificationTrigger")
//                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(13, 44))
//                    .build();
//        }
//
//        //kích hoạt sau một khoảng thời gian cố định từ thời điểm hiện tại,
//        // hãy sử dụng withIntervalInSeconds và withRepeatCount
//    public static Date time(String val) {
//        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        try {
//            return df.parse(val);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return new Date();
//        }
//    }
//}
//
//
//
//
