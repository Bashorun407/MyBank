package com.akinovapp.mybank.bank.controller;

import com.akinovapp.mybank.bank.dto.Data;
import com.akinovapp.mybank.bank.dto.TransactionRequest;
import com.akinovapp.mybank.bank.dto.TransferRequest;
import com.akinovapp.mybank.bank.dto.UserDto;
import com.akinovapp.mybank.bank.entity.User;
import com.akinovapp.mybank.bank.service.UserServiceImpl;
import com.akinovapp.mybank.response.ResponsePojo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth/myBank")
public class UserController {


    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    //1) Method to register/create user
    @PostMapping("/createUser")
    public ResponsePojo<User> createUser(@RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }

    //2) Method to List All users
    @GetMapping("/allUsers")
    public ResponsePojo<List<User>> fetchAllUsers(){
        return userService.fetchAllUsers();
    }

    //3) Method to find users by Id
    @GetMapping("/userById/{id}")
    public ResponsePojo<Optional<User>> findUserById(@PathVariable(name = "id") Long id){
        return userService.findUserById(id);
    }

    @GetMapping("/userByAccountNumber/{accountNumber}")
    public ResponsePojo<Optional<User>> findUserByAccountNumber(@PathVariable(name = "accountNumber") String accountNumber){
        return  userService.findUserByAccountNumber(accountNumber);
    }

    @GetMapping("/userByEmail/{email}")
    public ResponsePojo<Optional<User>> findUserByEmail(@PathVariable(name = "email") String email){
        return  userService.findUserByEmail(email);
    }
    //4) Method to display account balance to user
    @GetMapping("/balanceEnquiry/{accountNumber}")
    public ResponsePojo<Data> balanceEnquiry(@PathVariable(name = "accountNumber") String accountNumber){
        return userService.balanceEnquiry(accountNumber);
    }

    //5) Method to credit account
    @PutMapping("/creditAccount")
    public ResponsePojo<Data> creditAccount(@RequestBody TransactionRequest transactionRequest){
        return userService.creditAccount(transactionRequest);
    }

    //6) Method to debit account
    @PutMapping("/debitAccount")
    public ResponsePojo<Data> debitAccount(@RequestBody TransactionRequest transactionRequest){
        return userService.debitAccount(transactionRequest);
    }

    @PutMapping("/transferAmount")
    public ResponsePojo<Data> transferAmount(@RequestBody TransferRequest transferRequest){
        return userService.transferAmount(transferRequest);
    }
    //7) Method to update user's details
    @PutMapping("/updateUserDetail")
    public ResponsePojo<User> updateUserDetail(@RequestBody UserDto userDto){
        return userService.updateUserDetail(userDto);
    }

    //8) Method to deactivate user
    @DeleteMapping("/deleteUserDetail")
    public ResponsePojo<User> deleteUserDetail(@RequestBody UserDto userDto){
        return userService.deleteUserDetail(userDto);
    }
}

