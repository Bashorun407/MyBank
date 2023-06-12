package com.akinovapp.mybank.bank.service;

import com.akinovapp.mybank.bank.dto.TransactionDto;
import com.akinovapp.mybank.bank.entity.Transaction;
import com.akinovapp.mybank.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements ITransactionService{

    private final TransactionRepository transactionRepository;

    //Class constructor
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
   public void saveTransaction(TransactionDto transactionDto)
    {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .build();

        transactionRepository.save(transaction);
    }
}
