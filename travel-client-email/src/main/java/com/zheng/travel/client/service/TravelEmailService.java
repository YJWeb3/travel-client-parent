package com.zheng.travel.client.service;

import com.zheng.travel.client.vo.EmailResult;
import com.zheng.travel.client.vo.ToEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Slf4j
@Service
public class TravelEmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailTemplate emailTemplate;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * @param toEmail
     * @return
     */
    public Boolean sendEmail(ToEmail toEmail) {
        EmailResult emailResult = emailTemplate.getInstance(toEmail.getCode(),null);
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom(from);
        //谁要接收
        message.setTo(toEmail.getTos());
        //邮件标题
        message.setSubject(toEmail.getSubject());
        //邮件内容
        message.setText(emailResult.getHtmlTemplate());
        try {
            mailSender.send(message);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param toEmail
     * @return
     */
    public Boolean sendEmailHTML(ToEmail toEmail) {
        try {
            EmailResult emailResult = emailTemplate.getInstance(toEmail.getCode(),null);
            //创建一个MINE消息
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            //谁发
            minehelper.setFrom(from);
            //谁要接收
            minehelper.setTo(toEmail.getTos());
            //邮件主题
            minehelper.setSubject(toEmail.getSubject());
            //邮件内容   true 表示带有附件或html
            minehelper.setText(emailResult.getHtmlTemplate(), true);
            mailSender.send(message);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
