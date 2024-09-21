package com.zheng.travel.client.task.quartz;//package com.zheng.travel.client.task.quartz;
//
//import org.quartz.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//@Configuration
//public class MyQuartzJobConfiguration {
//
//    @Bean
//    public JobDetail jobDetail() {
//        JobDetail jobDetail = JobBuilder.newJob(MyQuartzJob.class)
//                .withIdentity("job1", "group1")
//                .storeDurably()
//                .build();
//        return jobDetail;
//    }
//
//
//    @Bean
//    public Trigger trigger() {
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .forJob(jobDetail())
//                .withIdentity("trigger1", "group1")
//                .startNow()
//                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
//                .build();
//        return trigger;
//    }
//}
