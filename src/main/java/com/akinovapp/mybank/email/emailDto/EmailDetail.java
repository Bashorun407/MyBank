package com.akinovapp.mybank.email.emailDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDetail {
    private String recipient;
    private String emailSubject;
    private String emailBody;
    private String attachment;
}
