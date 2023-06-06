package com.akinovapp.mybank.bank.entity;


import javax.persistence.*;

import lombok.*;

import java.math.BigDecimal;
@Data
@Entity(name = "transaction")
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", unique = true)
    private Long transactionId;
    private String transactionType;
    private String accountNumber;
    private BigDecimal amount;
}
