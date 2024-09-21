package com.zheng.travel.client.controller.nacos;

import com.zheng.travel.client.anno.login.LoginCheck;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RefreshScope
@LoginCheck
@Api(tags = "配置中心")
public class HelloNacosController {

    @Value("${travel.message.count}")
    private Integer count;


    @GetMapping("/nacoscount")
    public String nacoscount() {
        return count + "";
    }

}
