package com.zheng.travel.client.middle.jdk;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//模拟锁机制的线程类
public class LockThread implements Runnable {

    private int count;

    //构造方法
    public LockThread(int count) {
        this.count = count;
    }

    /**
     * 线程操作共享资源的方法体-不加同步锁
     */
    @Override
    public void run() {
        try {
            //执行10次访问共享的操作
            for (int i = 0; i < 10; i++) {
                // synchronized(类，属性，对象) ,这里东西在内存里面必须是唯一的
                synchronized (SysConstant.amount) {
                    //通过传进来的金额(可正、可负)执行叠加操作
                    SysConstant.amount = SysConstant.amount + count;
                    //打印每次操作完账户的余额
//                    log.info("此时账户余额为：{}", SysConstant.amount);
                }
            }
        } catch (Exception e) {
            //有异常情况时直接进行打印
            e.printStackTrace();
        }
    }
}
