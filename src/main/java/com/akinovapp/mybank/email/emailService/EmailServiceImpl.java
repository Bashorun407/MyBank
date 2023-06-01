package com.akinovapp.mybank.email.emailService;

import com.akinovapp.mybank.email.emailDto.EmailDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
public class EmailServiceImpl implements IEmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderMail;

    //Sending a simple mail
    @Override
   public String sendSimpleEmail(EmailDetail emailDetail){
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(senderMail);
            simpleMailMessage.setTo(emailDetail.getRecipient());
            simpleMailMessage.setSubject(emailDetail.getEmailSubject());
            simpleMailMessage.setText(emailDetail.getEmailBody());

            javaMailSender.send(simpleMailMessage);

            return "Mail sent successfully";
        }
        catch (MailException me){
            throw new RuntimeException(me);
        }
    }

    //Sending a mail with attachment (Mime mail)
    @Override
   public String sendEmailWithAttachment(EmailDetail emailDetail){
       MimeMessage mimeMessage = javaMailSender.createMimeMessage();
       MimeMessageHelper mimeMessageHelper;

        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(senderMail);
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setSubject(emailDetail.getEmailSubject());
            mimeMessageHelper.setText(emailDetail.getEmailBody());

            FileSystemResource fileSystemResource = new FileSystemResource(new File(emailDetail.getAttachment()));
            mimeMessageHelper.addAttachment(Objects.requireNonNull(fileSystemResource.getFilename()), fileSystemResource);

            javaMailSender.send(mimeMessage);
            return "Mime mail sent successfully";
        }
        catch (MailException | MessagingException me){
            throw new RuntimeException(me);
        }
    }
}
