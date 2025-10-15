package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Dto.EmailDto;
import com.globalskills.user_service.Account.Exception.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    JavaMailSender javaMailSender;


    @Autowired
    TemplateEngine templateEngine;

    public boolean sendEmailVerify(EmailDto emailDetail)  {
        try {
            Context context = new Context();
            context.setVariable("name", emailDetail.getAccount().getUsername());
            context.setVariable("button", "Click Here to verify");
            context.setVariable("link", emailDetail.getLink());
            context.setVariable("date", emailDetail.getCreatedDate());
            String template = templateEngine.process("EmailVerify", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom("globalskillsexe@gmail.com");
            messageHelper.setTo(emailDetail.getAccount().getEmail());
            messageHelper.setText(template,true);
            messageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(message);
            return true;
        }catch(MessagingException e){
            throw new EmailException("Error sending  email, please check your mail again",HttpStatus.BAD_REQUEST);
        }
//            ApiClient client = Postmark.getApiClient(postmarkApiToken);
//            Message message = new Message(senderEmail, emailDetail.getAccount().getEmail(), emailDetail.getSubject(), template);
//            message.setMessageStream("transactional");
//            MessageResponse response = client.deliverMessage(message);
//            if (response.getErrorCode() == 0) {
//                logger.info("Send verify email to: {}", emailDetail.getAccount().getEmail());
//                return true;
//            } else {
//                logger.error("Postmark error sending verify email: {} - {}", response.getErrorCode(), response.getMessage());
//                throw new EmailException("Error sending email: " + response.getMessage(), HttpStatus.BAD_REQUEST);
//            }
//        } catch (EmailException e) {
//            throw e;
//        } catch (Exception e) {
//            logger.error("Unexpected error sending verify email: {}", e.getMessage(), e);
//            throw new EmailException("Unexpected error when sending email", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    public void sendEmailResetPassword(EmailDto emailDetail)  {
        try {
            Context context = new Context();
            context.setVariable("name", emailDetail.getAccount().getUsername());
            context.setVariable("button", "Reset Password");
            context.setVariable("link", emailDetail.getLink());
            context.setVariable("date", emailDetail.getCreatedDate());
            String template = templateEngine.process("EmailResetPassword", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom("globalskillsexe@gmail.com");
            messageHelper.setTo(emailDetail.getAccount().getEmail());
            messageHelper.setText(template,true);
            messageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(message);
        }catch(MessagingException e){
            throw new EmailException("Error sending  email, please check your mail again",HttpStatus.BAD_REQUEST);
        }
//            ApiClient client = Postmark.getApiClient(postmarkApiToken);
//            Message message = new Message(senderEmail, emailDetail.getAccount().getEmail(), emailDetail.getSubject(), template);
//            message.setMessageStream("transactional");
//            MessageResponse response = client.deliverMessage(message);
//            if (response.getErrorCode() == 0) {
//                logger.info("Send verify email to: {}", emailDetail.getAccount().getEmail());
//            } else {
//                logger.error("Postmark error sending verify email: {} - {}", response.getErrorCode(), response.getMessage());
//                throw new EmailException("Error sending email: " + response.getMessage(), HttpStatus.BAD_REQUEST);
//            }
//        } catch (EmailException e) {
//            throw e;
//        } catch (Exception e) {
//            logger.error("Unexpected error sending verify email: {}", e.getMessage(), e);
//            throw new EmailException("Unexpected error when sending email", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    public void sendEmailApproveCV(EmailDto emailDetail){
        try {
            Context context = new Context();
            context.setVariable("name", emailDetail.getAccount().getUsername());
            context.setVariable("button", "HomePage");
            context.setVariable("link", emailDetail.getLink());
            context.setVariable("date", emailDetail.getCreatedDate());
            String template = templateEngine.process("EmailApproveCV", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom("globalskillsexe@gmail.com");
            messageHelper.setTo(emailDetail.getAccount().getEmail());
            messageHelper.setText(template,true);
            messageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(message);
        }catch(MessagingException e){
            throw new EmailException("Error sending  email, please check your mail again",HttpStatus.BAD_REQUEST);
        }
    }

    public void sendEmailRejectCV(EmailDto emailDetail){
        try {
            Context context = new Context();
            context.setVariable("name", emailDetail.getAccount().getUsername());
            context.setVariable("button", "HomePage");
            context.setVariable("link", emailDetail.getLink());
            context.setVariable("date", emailDetail.getCreatedDate());
            String template = templateEngine.process("EmailRejectCV", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom("globalskillsexe@gmail.com");
            messageHelper.setTo(emailDetail.getAccount().getEmail());
            messageHelper.setText(template,true);
            messageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(message);
        }catch(MessagingException e){
            throw new EmailException("Error sending  email, please check your mail again",HttpStatus.BAD_REQUEST);
        }
    }
}