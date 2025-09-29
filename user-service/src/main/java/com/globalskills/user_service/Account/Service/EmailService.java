package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Dto.EmailDto;
import com.globalskills.user_service.Account.Exception.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TemplateEngine templateEngine;


    public boolean sendEmailVerify(EmailDto emailDetail)  {
        try{
            Context context = new Context();
            context.setVariable("name",emailDetail.getAccount().getFullName());
            context.setVariable("button","Click Here to verify");
            context.setVariable("link",emailDetail.getLink());
            context.setVariable("date",emailDetail.getCreatedDate());
            String template = templateEngine.process("EmailVerify",context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom("admin@gmail.com");
            messageHelper.setTo(emailDetail.getAccount().getEmail());
            messageHelper.setText(template,true);
            messageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(message);
            return true;
        }catch(MessagingException e){
            throw new EmailException("Error sending  email, please check your mail again", HttpStatus.BAD_REQUEST);
        }
    }

    public void sendEmailResetPassword(EmailDto emailDetail)  {
        try{
            Context context = new Context();
            context.setVariable("name",emailDetail.getAccount().getFullName());
            context.setVariable("button","Reset Password");
            context.setVariable("link",emailDetail.getLink());
            context.setVariable("date",emailDetail.getCreatedDate());
            String template = templateEngine.process("EmailResetPassword",context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom("admin@gmail.com");
            messageHelper.setTo(emailDetail.getAccount().getEmail());
            messageHelper.setText(template,true);
            messageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(message);
        }catch(MessagingException e){
            throw new EmailException("Error sending  email, please check your mail again",HttpStatus.BAD_REQUEST);
        }
    }

}
