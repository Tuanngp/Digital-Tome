//package com.fpt.swp391.group6.DigitalTome.quartzJob;
//
//import org.quartz.*;
//import org.quartz.impl.DirectSchedulerFactory;
//import org.quartz.impl.StdSchedulerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.DependsOn;
//
//public class SchedulerConfig {
//
//
//    private final JobDetail jobDetail;
//    private final Trigger trigger;
//
//    public SchedulerConfig(@Qualifier("jobDetail") JobDetail jobDetail, @Qualifier("jobTrigger") Trigger trigger) {
//        this.jobDetail = jobDetail;
//        this.trigger = trigger;
//    }
//
//
//    @DependsOn({"jobDetail","jobTrigger"})
//    public void initialJob(){
//        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
//
//        try {
//            Scheduler scheduler = schedulerFactory.getScheduler();
//            scheduler.scheduleJob( jobDetail, trigger);
//            scheduler.start();;
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
