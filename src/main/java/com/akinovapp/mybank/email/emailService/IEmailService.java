package com.akinovapp.mybank.email.emailService;

import com.akinovapp.mybank.email.emailDto.EmailDetail;

public interface IEmailService {

    String sendSimpleEmail(EmailDetail emailDetail);
    String sendEmailWithAttachment(EmailDetail emailDetail);
}

