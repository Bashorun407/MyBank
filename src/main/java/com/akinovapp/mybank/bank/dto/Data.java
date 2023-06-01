package com.akinovapp.mybank.bank.dto;

import lombok.Builder;

import java.math.BigDecimal;

@lombok.Data
@Builder
public class Data {
    private String accountNumber;
    private String accountName;
    private BigDecimal accountBalance;
}
