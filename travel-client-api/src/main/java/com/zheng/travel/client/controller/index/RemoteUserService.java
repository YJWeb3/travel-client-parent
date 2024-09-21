//package com.zheng.travel.client.controller.index;//package com.zheng.travel.controller.index;
//
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.concurrent.Callable;
//import java.util.concurrent.FutureTask;
//
//@Service
//public class RemoteUserService {
//
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    /**
//     * 查询多个系统得数据，合并返回
//     * @return
//     */
//    public Object getUserInfo() throws  Exception{// thread-0
//
//
//        // 线程1
//        Callable<JSONObject> callable1 = new Callable<JSONObject>() {
//            @Override
//            public JSONObject call() throws Exception {
//                // 1 : 订单信息
//                long userinfotime1 = System.currentTimeMillis();
//                String value = restTemplate.getForObject("http://localhost:8888/selectAll",String.class);
//                JSONObject jsonObject = JSONObject.parseObject(value);
//                System.out.println("userinfotime1 获取用户得订单数信息耗时： " + (System.currentTimeMillis() - userinfotime1)+"ms");
//                return jsonObject;
//            }
//        };
//
//
//        // 线程3
//        Callable<JSONObject> callable2 = new Callable<JSONObject>() {
//            @Override
//            public JSONObject call() throws Exception {
//                // 2 : 获取用户信息
//                long userinfotime2 = System.currentTimeMillis();
//                String value2 = restTemplate.getForObject("http://localhost:8888/getuser/15074816437",String.class);
//                JSONObject jsonObject2 = JSONObject.parseObject(value2);
//                System.out.println("userinfotime2 获取用户信息耗时： " + (System.currentTimeMillis() - userinfotime2)+"ms");
//                return jsonObject2;
//            }
//        };
//
//
//        // 线程3
//        Callable<JSONObject> callable3 = new Callable<JSONObject>() {
//            @Override
//            public JSONObject call() throws Exception {
//                // 3 : 获取足迹
//                long userinfotime3 = System.currentTimeMillis();
//                String value3 = restTemplate.getForObject("http://localhost:8888/getuser/15074816437",String.class);
//                JSONObject jsonObject3 = JSONObject.parseObject(value3);
//                System.out.println("userinfotime3 获取足迹耗时： " + (System.currentTimeMillis() - userinfotime3)+"ms");
//                return jsonObject3;
//            }
//        };
//
//
//        //就是一个线程任务1
//        FutureTask<JSONObject> futureTask1 = new FutureTask<>(callable1);
//        new Thread(futureTask1).start();//3s
//        //就是一个线程任务2
//        FutureTask<JSONObject> futureTask2 = new FutureTask<>(callable2);
//        new Thread(futureTask2).start();//1s
//        //就是一个线程任务3
//        FutureTask<JSONObject> futureTask3 = new FutureTask<>(callable3);
//        new Thread(futureTask3).start();//3s
//
//
//
//        //  结果集的返回
//        JSONObject mainobj = new JSONObject();
//        // 然后回把订单的信息统一放入到mainobj
//        mainobj.putAll(futureTask1.get());//阻塞
//        // 然后的用户的信息统一放入到mainobj
//        mainobj.putAll(futureTask2.get());//阻塞
//        // 然后的足迹统一放入到mainobj
//        mainobj.putAll(futureTask3.get());//阻塞
//
//        return mainobj;
//    }
//
//
//}
