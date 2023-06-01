package com.akinovapp.mybank.bank.service;

import com.akinovapp.mybank.bank.dto.TransactionDto;

public interface ITransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
