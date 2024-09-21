package com.zheng.travel.client.middle.jdk;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

@Slf4j
public class LockOneTest {

    public static void main(String args[]) {
        // 存钱
        LockThread A = new LockThread(100);//A.run()
        Thread tAdd = new Thread(A,"存钱");

        // 取钱
        LockThread B = new LockThread(-100);//B.run()
        Thread tSub = new Thread(B,"取钱");
        tAdd.start();//线程1
        tSub.start();//线程2
    }

}

