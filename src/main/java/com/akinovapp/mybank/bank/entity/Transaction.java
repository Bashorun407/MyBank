package com.akinovapp.mybank.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "transaction")
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "amount")
    private BigDecimal amount;
}
