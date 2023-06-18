package com.akinovapp.mybank.bank.service;

import com.akinovapp.mybank.bank.dto.Data;
import com.akinovapp.mybank.bank.dto.TransactionRequest;
import com.akinovapp.mybank.bank.dto.TransferRequest;
import com.akinovapp.mybank.bank.dto.UserDto;
import com.akinovapp.mybank.bank.entity.User;
import com.akinovapp.mybank.response.ResponsePojo;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    ResponsePojo<User> createUser(UserDto userDto);
    ResponsePojo<List<User>> fetchAllUsers();
    ResponsePojo<Optional<User>> findUserById(Long id);
    ResponsePojo<Optional<User>> findUserByAccountNumber(String accountNumber);
    ResponsePojo<Optional<User>> findUserByEmail(String email);
    ResponsePojo<Data> balanceEnquiry(String accountNumber);

    ResponsePojo<Data> creditAccount(TransactionRequest transactionRequest);
    ResponsePojo<Data> debitAccount(TransactionRequest transactionRequest);
    ResponsePojo<Data> transferAmount(TransferRequest transferRequest);
    ResponsePojo<User> updateUserDetail(UserDto userDto);
    ResponsePojo<User> deleteUserDetail(UserDto userDto);

}

