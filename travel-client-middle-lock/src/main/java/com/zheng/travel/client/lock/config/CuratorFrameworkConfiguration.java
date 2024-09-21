package com.zheng.travel.client.lock.config; /**
 * Created by Administrator on 2019/3/13.
 */

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用化配置
 *
 * @Date: 2019/3/13 8:38
 * @Link：微信-debug0868 QQ-1948831260
 **/
@Configuration
public class CuratorFrameworkConfiguration {

    //读取环境变量的实例
    //@Autowired
    //private Environment env;
    @Value("${zk.host}")
    private String host;
    @Value("${zk.namespace}")
    private String namespace;


    //自定义注入Bean-ZooKeeper高度封装过的客户端Curator实例
    @Bean
    public CuratorFramework curatorFramework() {
        //创建CuratorFramework实例
        //（1）创建的方式是采用工厂模式进行创建；
        //（2）指定了客户端连接到ZooKeeper服务端的策略：这里是采用重试的机制(5次，每次间隔1s)
//        CuratorFramework curatorFramework= CuratorFrameworkFactory.builder()
//                .connectString(env.getProperty("zk.host")).namespace(env.getProperty("zk.namespace"))
//                .retryPolicy(new RetryNTimes(5,1000)).build();
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(host).namespace(namespace)
                .retryPolicy(new RetryNTimes(5, 1000)).build();
        curatorFramework.start();
        //返回CuratorFramework实例
        return curatorFramework;
    }

}