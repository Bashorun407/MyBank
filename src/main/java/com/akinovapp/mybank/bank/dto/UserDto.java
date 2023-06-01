package com.akinovapp.mybank.bank.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class UserDto {

    private String firstName;
    private String lastName;
    private String otherName;
    private String gender;
    private String home_address;
    private String stateOfOrigin;
    private String accountNumber;
    private BigDecimal accountBalance;
    private String email;
    private String phoneNumber;
    private String alternativeNumber;
    private String status;
    private LocalDate dateOfBirth;
}
