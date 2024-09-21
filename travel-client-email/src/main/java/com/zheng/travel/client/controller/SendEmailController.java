package com.zheng.travel.client.controller;

import com.zheng.travel.client.service.TravelEmailService;
import com.zheng.travel.client.vo.ToEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendEmailController {

    @Autowired
    private TravelEmailService TRAVELEmailService;

    @GetMapping("/sendemail")
    public String commonEmail(ToEmail toEmail) {
        toEmail.setSubject("注册邮件");
        return TRAVELEmailService.sendEmail(toEmail) ? "success" : "fail";
    }


    @GetMapping("/sendemail2")
    public String htmlEmail(ToEmail toEmail) {
        return TRAVELEmailService.sendEmailHTML(toEmail) ? "success" : "fail";
    }

}
