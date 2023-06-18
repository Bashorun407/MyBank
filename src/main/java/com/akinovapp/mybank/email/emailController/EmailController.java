package com.akinovapp.mybank.email.emailController;

import com.akinovapp.mybank.email.emailDto.EmailDetail;
import com.akinovapp.mybank.email.emailService.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/myBank")
public class EmailController {
    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping("/simpleEmail")
    public String sendSimpleEmail(@RequestBody EmailDetail emailDetail){
        return emailService.sendSimpleEmail(emailDetail);
    }

    @PostMapping("/mimeMessage")
    public String sendEmailWithAttachment(@RequestBody EmailDetail emailDetail){
        return emailService.sendEmailWithAttachment(emailDetail);
    }
}

