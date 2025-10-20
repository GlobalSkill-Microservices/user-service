package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Dto.EmailDto;
import com.globalskills.user_service.Account.Exception.EmailException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
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

import java.io.IOException;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    SendGrid sendGrid;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    public boolean sendEmailVerify(EmailDto emailDetail)  {
            sendEmailWithTemplate(emailDetail,"EmailVerify", "Verify");
            return true;
        }


    public void sendEmailResetPassword(EmailDto emailDetail) {
        sendEmailWithTemplate(emailDetail, "EmailResetPassword", "Reset Password");
    }

    public void sendEmailApproveCV(EmailDto emailDetail) {
        sendEmailWithTemplate(emailDetail, "EmailApproveCV", "HomePage");
    }

    public void sendEmailRejectCV(EmailDto emailDetail) {
        sendEmailWithTemplate(emailDetail, "EmailRejectCV", "HomePage");
    }

        private void sendEmailWithTemplate(EmailDto emailDetail, String templateName, String buttonText) {
            Context context = new Context();
            context.setVariable("name", emailDetail.getAccount().getUsername());
            context.setVariable("button", buttonText);
            context.setVariable("link", emailDetail.getLink());
            context.setVariable("date", emailDetail.getCreatedDate());
            String htmlContent = templateEngine.process(templateName, context);

            Email from = new Email(fromEmail);
            String subject = emailDetail.getSubject();
            Email to = new Email(emailDetail.getAccount().getEmail());
            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, subject, to, content);

            // 3. Gửi mail và xử lý kết quả
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                logger.info("Sending '{}' email to {}", templateName, to.getEmail());
                Response response = sendGrid.api(request);

                if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                    logger.error("Failed to send email '{}'. Status: {}, Body: {}",
                            templateName, response.getStatusCode(), response.getBody());
                    throw new EmailException("Error sending email via SendGrid", HttpStatus.BAD_GATEWAY);
                }

                logger.info("Successfully sent '{}' email to {}. Status: {}", templateName, to.getEmail(), response.getStatusCode());

            } catch (IOException ex) {
                logger.error("IO Exception while sending email '{}' to {}", templateName, to.getEmail(), ex);
                throw new EmailException("Error processing email request", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }