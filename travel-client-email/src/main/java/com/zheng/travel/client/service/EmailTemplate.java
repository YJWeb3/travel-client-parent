package com.zheng.travel.client.service;

import com.zheng.travel.client.vo.EmailResult;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplate {

    /**
     * 注册模板
     *
     * @return
     */
    public EmailResult regTemplateText() {
        // 验证码生成随机数
        int randomNum = (int) ((Math.random() * 9 + 1) * 100000);
        // 这里可能需要放入到redis/session中
        String textTemplate = "欢迎你注册平台，你得注册码是：" + randomNum;
        EmailResult emailResult = new EmailResult();
        emailResult.setResult(randomNum);
        emailResult.setHtmlTemplate(textTemplate);
        return emailResult;
    }


    /**
     * 注册模板
     *
     * @return
     */
    public EmailResult regTemplateHTML() {
        // 验证码生成随机数
        int randomNum = (int) ((Math.random() * 9 + 1) * 100000);
        // 这里可能需要放入到redis/session中
        String htmlTemplate = "<html>\n" +
                "<body>\n" +
                " <img src='https://www.kuangstudy.com/assert/images/index_topleft_logo_black.png'>    " +
                "<h1>欢迎你注册平台，你得注册码是：" + randomNum + "</h1>\n" +
                "</body>\n" +
                "</html>";
        EmailResult emailResult = new EmailResult();
        emailResult.setResult(randomNum);
        emailResult.setHtmlTemplate(htmlTemplate);
        return emailResult;
    }


    /**
     * 告警模板
     *
     * @return
     */
    public EmailResult platformError(Exception ex) {
        // 这里可能需要放入到redis/session中
        String htmlTemplate = "<html>\n" +
                "<body>\n" +
                " <img src='https://www.kuangstudy.com/assert/images/index_topleft_logo_black.png'>    " +
                "<h1>平台出现了故障了，故障信息：" + ex.getMessage() + "</h1>\n" +
                "</body>\n" +
                "</html>";
        EmailResult emailResult = new EmailResult();
        emailResult.setResult("");
        emailResult.setHtmlTemplate(htmlTemplate);
        return emailResult;
    }


    /**
     * @param code 1 : 注册模板 2：异常告警
     * @param ex   code =2 有效
     * @return
     */
    public EmailResult getInstance(int code, Exception ex) {
        if (code == 1) {
            return regTemplateText();
        }
        if (code == 2) {
            return regTemplateHTML();
        }
        if (code == 3) {
            return platformError(ex);
        }
        return null;
    }

}
