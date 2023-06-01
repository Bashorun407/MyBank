package com.akinovapp.mybank.bank.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferRequest {

    private String senderAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
}


