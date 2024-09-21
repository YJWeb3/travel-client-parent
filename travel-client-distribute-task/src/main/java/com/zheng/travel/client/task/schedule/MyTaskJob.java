package com.zheng.travel.client.task.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyTaskJob {

    @Autowired
    private MyTaskJobProcess myTaskJobProcess;

    @Scheduled(fixedRate = 3000)
    public void process1() throws InterruptedException {
        log.info("process1.............start");
        myTaskJobProcess.doProcess1();
        log.info("process1.............end");
    }

//    @Scheduled(fixedRate = 3000)
//    public void process2() throws InterruptedException {
//        log.info("process2.............start");
//        Thread.sleep(5000);
//        log.info("process2.............end");
//    }
}
