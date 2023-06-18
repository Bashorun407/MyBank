package com.akinovapp.mybank.bank.service;

import com.akinovapp.mybank.bank.dto.*;

import com.akinovapp.mybank.bank.entity.User;
import com.akinovapp.mybank.bank.entity.UserRole;
import com.akinovapp.mybank.bank.repository.UserRepository;
import com.akinovapp.mybank.config.JwtService;
import com.akinovapp.mybank.email.emailDto.EmailDetail;
import com.akinovapp.mybank.email.emailService.EmailServiceImpl;

import com.akinovapp.mybank.exception.ApiException;
import com.akinovapp.mybank.response.AuthenticationResponse;
import com.akinovapp.mybank.response.ResponsePojo;
import com.akinovapp.mybank.response.ResponseUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService{


    private final UserRepository userRepository;

    private final TransactionServiceImpl transactionService;

    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    //Class Constructor
    public UserServiceImpl(UserRepository userRepository, TransactionServiceImpl transactionService,
                           EmailServiceImpl emailService, PasswordEncoder passwordEncoder,
                           JwtService jwtService, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    //1) Method to create user
    @Override
    public ResponsePojo<User> createUser(UserDto userDto){


        boolean userExist = userRepository.existsByEmail(userDto.getEmail());

        //Check that user does not exist...else, throw an exception
        if (userExist)
            throw new ApiException(ResponseUtils.USER_EXISTS_MESSAGE);

        //If no user with similar email is not found, create a new user
        User newUser = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .otherName(userDto.getOtherName())
                .gender(userDto.getGender())
                .home_address(userDto.getHome_address())
                .stateOfOrigin(userDto.getStateOfOrigin())
                .accountNumber(ResponseUtils.generateAccountNumber(10))
                .accountBalance(BigDecimal.valueOf(1500.00))
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword())) //'encrypting' user password before saving to repository
                .role(UserRole.USER)
                .phoneNumber(userDto.getPhoneNumber())
                .alternativeNumber(userDto.getAlternativeNumber())
                .status(ResponseUtils.ACTIVE)
                .dateOfBirth(userDto.getDateOfBirth())
                .createdAt(LocalDateTime.now())
                .build();
        //Saving new user to repository
         User savedUser = userRepository.save(newUser);

         //Spring security to register
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();

        //Call to register user
        authenticationService.register(registerRequest);

        //Spring security to authenticate user
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();

        //call to authenticate user

        AuthenticationResponse authResponse = authenticationService.authenticate(authenticationRequest);

        String accountDetails = savedUser.getLastName() + "," + savedUser.getFirstName() + " " + savedUser.getOtherName()
                 + " \n" + savedUser.getAccountNumber() + "\n" + savedUser.getAccountBalance();

        //Creating an object of EmailDetail
        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(userDto.getEmail())
                        .emailSubject("SUCCESSFUL ACCOUNT CREATION")
                                .emailBody(String.format("Dear %s %s, your account has been successfully created." +
                                        "\n Thanks for banking with us.\n Akinov Bank \n" +
                                        "\n Account Details:\n"
                                        + accountDetails, userDto.getLastName(), userDto.getFirstName()))
                                        .build();
        //Send email to user
        emailService.sendSimpleEmail(emailDetail);
        //Return response to caller
        ResponsePojo<User> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESS);
        responsePojo.setMessage(ResponseUtils.USER_REGISTERED_SUCCESS + "\n authentication: " + authResponse);
        responsePojo.setData(savedUser);

        return responsePojo;
    }

    //2) Method to List All users
    @Override
    public ResponsePojo<List<User>> fetchAllUsers(){

        List<User> userList = userRepository.findAll();

        //Using lambda expression to filter only users whose status reads ACTIVE
        List<User> collect = userList.stream().filter(x -> x.getStatus().equals(ResponseUtils.ACTIVE)).collect(Collectors.toList());
        ResponsePojo<List<User>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESS);
        responsePojo.setMessage(ResponseUtils.SUCCESS_MESSAGE);
        responsePojo.setData(collect);

        return responsePojo;
    }

    //3) Method to find users by Id
    @Override
    public ResponsePojo<Optional<User>> findUserById(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        //if user does not exist, throw an exception
        userOptional.orElseThrow(()-> new ApiException(String.format("User with this id: %d does not exist", id)));
        //checking that IN-ACTIVE USERS ARE NOT CALLED
        if(userOptional.get().getStatus().equals(ResponseUtils.NON_ACTIVE))
            throw new ApiException("Account of this user has been deactivated.");

        ResponsePojo<Optional<User>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESS);
        responsePojo.setMessage(ResponseUtils.USER_FOUND_MESSAGE);
        responsePojo.setData(userOptional);

        return responsePojo;
    }


    //3b) Method to find users by Id
    @Override
    public ResponsePojo<Optional<User>> findUserByAccountNumber(String accountNumber){
        Optional<User> userOptional = userRepository.findByAccountNumber(accountNumber);

        //if user does not exist, throw an exception
        userOptional.orElseThrow(()-> new ApiException(String.format("User with this account number: %s does not exist", accountNumber)));
        //checking that IN-ACTIVE USERS ARE NOT CALLED
        if(userOptional.get().getStatus().equals(ResponseUtils.NON_ACTIVE))
            throw new ApiException("Account of this user has been deactivated.");


        ResponsePojo<Optional<User>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESS);
        responsePojo.setMessage(ResponseUtils.USER_FOUND_MESSAGE);
        responsePojo.setData(userOptional);

        return responsePojo;
    }

    @Override
    public ResponsePojo<Optional<User>> findUserByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);

        //if user does not exist, throw an exception
        userOptional.orElseThrow(()-> new ApiException(String.format("User with this account number: %s does not exist", email)));
        //checking that IN-ACTIVE USERS ARE NOT CALLED
        if(userOptional.get().getStatus().equals(ResponseUtils.NON_ACTIVE))
            throw new ApiException("Account of this user has been deactivated.");


        ResponsePojo<Optional<User>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESS);
        responsePojo.setMessage(ResponseUtils.USER_FOUND_MESSAGE);
        responsePojo.setData(userOptional);

        return responsePojo;
    }

    //4) Method to display account balance to user
    @Override
    public ResponsePojo<Data> balanceEnquiry(String accountNumber){
        Optional<User> userOptional = userRepository.findByAccountNumber(accountNumber);
        userOptional.orElseThrow(()-> new ApiException(ResponseUtils.USER_NOT_FOUND_MESSAGE));

        User user = userOptional.get();

        String accountDetails = user.getLastName() + "," + user.getFirstName() + " " + user.getOtherName()
                + " \n Account Number: " + user.getAccountNumber() + "\n Account Balance: " + user.getAccountBalance();

        //Creating an object of EmailDetail
        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(user.getEmail())
                .emailSubject("ACCOUNT DETAILS")
                .emailBody(String.format("Dear %s %s, \n In response to your request for your account details," +
                        " your account details are given below:  ." +
                        "\n Thanks for banking with us.\n Akinov Bank \n" +
                                "\n Account Details:\n" +
                        accountDetails, user.getLastName(), user.getFirstName()))
                .build();
        //Send email notification to user
        emailService.sendSimpleEmail(emailDetail);
        //If user exists, display account details
        Data data = Data.builder()
                .accountName(user.getLastName() + ", " + user.getFirstName() + " " + user.getOtherName())
                .accountNumber(user.getAccountNumber())
                .accountBalance(user.getAccountBalance())
                .build();

        ResponsePojo<Data> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESS);
        responsePojo.setMessage(ResponseUtils.SUCCESSFUL_TRANSACTION);
        responsePojo.setData(data);

        return responsePojo;

    }

    //5) Method to credit account
    @Override
    public ResponsePojo<Data> creditAccount(TransactionRequest transactionRequest){

        Boolean existByAccountNumber = userRepository.existsByAccountNumber(transactionRequest.getAccountNumber());

        //checks that user with specified email exists
        if(!existByAccountNumber)
            throw new ApiException(ResponseUtils.USER_NOT_FOUND_MESSAGE);

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("CREDIT")
                .accountNumber(transactionRequest.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();

        //Transaction is saved to transaction database
        transactionService.saveTransaction(transactionDto);

        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber()).get();
        //New amount is added to user balance and user balance is updated
        user.setAccountBalance(user.getAccountBalance().add(transactionRequest.getAmount()));
        //balance is saved to user database
        userRepository.save(user);
        //Update user's bank details

        //updated information is saved to repository
        User savedUser = userRepository.save(user);

        String accountDetails = savedUser.getLastName() + "," + savedUser.getFirstName() + " " + savedUser.getOtherName()
                + " \n Account Number: " + savedUser.getAccountNumber() + "\n Account Balance: " + savedUser.getAccountBalance();

        //Creating an object of EmailDetail
        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(user.getEmail())
                .emailSubject("CREDIT ACCOUNT")
                .emailBody(String.format("Dear %s %s, your account has been credited with an amount of: " + transactionRequest.getAmount() +
                        "\n Thanks for banking with us.\n Akinov Bank \n" +
                                "\n Account Details:\n" +
                        accountDetails, user.getLastName(), user.getFirstName(), transactionRequest.getAmount()))
                .build();
        //Send email notification to user
        emailService.sendSimpleEmail(emailDetail);

        //Response to caller
        Data data = Data.builder()
                .accountName(user.getLastName() + " " + user.getFirstName() + " " + user.getOtherName())
                .accountNumber(user.getAccountNumber())
                .accountBalance(user.getAccountBalance())
                .build();

        //Collecting the data in responsePojo
        ResponsePojo<Data> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESSFUL_TRANSACTION);
        responsePojo.setMessage(ResponseUtils.SUCCESS_MESSAGE);
        responsePojo.setData(data);

        return responsePojo;
    }

    //6) Method to debit account
    @Override
    public ResponsePojo<Data> debitAccount(TransactionRequest transactionRequest){

        Boolean existByAccountNumber = userRepository.existsByAccountNumber(transactionRequest.getAccountNumber());

        //checks that user with specified email exists
        if(!existByAccountNumber)
            throw new ApiException(ResponseUtils.USER_NOT_FOUND_MESSAGE);

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("DEBIT")
                .accountNumber(transactionRequest.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();

        //Transaction is saved to transaction database
        transactionService.saveTransaction(transactionDto);

        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber()).get();
        //New amount is added to user balance and user balance is updated
        user.setAccountBalance(user.getAccountBalance().subtract(transactionRequest.getAmount()));

        //updated information is saved to repository
        User savedUser = userRepository.save(user);

        String accountDetails = savedUser.getLastName() + "," + savedUser.getFirstName() + " " + savedUser.getOtherName()
                + " \n Account Number: " + savedUser.getAccountNumber() + "\nAccount Balance: " + savedUser.getAccountBalance();

        //Creating an object of EmailDetail
        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(user.getEmail())
                .emailSubject("DEBIT ACCOUNT")
                .emailBody(String.format("Dear %s %s, your account has been debited with the amount of:" + transactionRequest.getAmount() +
                        "\n Thanks for banking with us.\n Akinov Bank \n" +
                                "\n Account Details:\n" +
                        accountDetails, user.getLastName(), user.getFirstName()))
                .build();
        //Send email notification to user
        emailService.sendSimpleEmail(emailDetail);

        //Response to caller
        Data data = Data.builder()
                .accountName(user.getLastName() + " " + user.getFirstName() + " " + user.getOtherName())
                .accountNumber(user.getAccountNumber())
                .accountBalance(user.getAccountBalance())
                .build();

        //Collecting the data in responsePojo
        ResponsePojo<Data> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESSFUL_TRANSACTION);
        responsePojo.setMessage(ResponseUtils.SUCCESS_MESSAGE);
        responsePojo.setData(data);

        return responsePojo;
    }


    //7) Method to transfer from an account to another
    public ResponsePojo<Data> transferAmount(TransferRequest transferRequest){
        //To debit sender's account
        TransactionRequest transactionRequestDebit = TransactionRequest.builder()
                .accountNumber(transferRequest.getSenderAccountNumber())
                .amount(transferRequest.getAmount())
                .build();
        //Calling debit method
        debitAccount(transactionRequestDebit);

        //To credit receiver's account
        TransactionRequest transactionRequestCredit = TransactionRequest.builder()
                .accountNumber(transferRequest.getDestinationAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        //Calling credit method
        creditAccount(transactionRequestCredit);

        //Generating Data to send to caller
        User senderDetails = userRepository.findByAccountNumber(transferRequest.getSenderAccountNumber()).get();
       Data userData = Data.builder()
                .accountName(senderDetails.getLastName() + " " + senderDetails.getFirstName())
                .accountNumber(senderDetails.getAccountNumber())
                .accountBalance(senderDetails.getAccountBalance())
                .build();

        //Response Pojo
        ResponsePojo<Data> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESSFUL_TRANSACTION);
        responsePojo.setMessage(ResponseUtils.SUCCESS_MESSAGE);
        responsePojo.setData(userData);

        return responsePojo;
    }

    //8) Method to update user's details
    @Override
    public ResponsePojo<User> updateUserDetail(UserDto userDto){
        Optional<User> userOptional = userRepository.findByAccountNumber(userDto.getAccountNumber());
        userOptional.orElseThrow(()-> new ApiException(ResponseUtils.USER_NOT_FOUND_MESSAGE));

        //The following are user updates
        User user = userOptional.get();
        user.setFirstName(userDto.getFirstName());
        user.setOtherName(userDto.getOtherName());
        user.setLastName(userDto.getLastName());
        user.setHome_address(userDto.getHome_address());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAlternativeNumber(userDto.getAlternativeNumber());
        user.setEmail(userDto.getEmail());
        user.setModifiedAt(LocalDateTime.now());

        //updated information is saved to repository
       User savedUser = userRepository.save(user);

        String accountDetails = savedUser.getLastName() + "," + savedUser.getFirstName() + " " + savedUser.getOtherName()
                + " \n Account Number: " + savedUser.getAccountNumber() + "\n" + " Account Balance: " + savedUser.getAccountBalance();

        //Creating an object of EmailDetail
        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(userDto.getEmail())
                .emailSubject("SUCCESSFUL ACCOUNT UPDATE")
                .emailBody(String.format("Dear %s %s, your account has been successfully updated." +
                        "\n Thanks for banking with us.\n Akinov Bank \n" +
                                "\n Account Details:\n" +
                        accountDetails, userDto.getLastName(), userDto.getFirstName()))
                .build();
        //Send email to user
        emailService.sendSimpleEmail(emailDetail);

        //response is given back to the caller
        ResponsePojo<User> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESS);
        responsePojo.setMessage(ResponseUtils.SUCCESS_MESSAGE);
        responsePojo.setData(user);

        return responsePojo;
    }

    //8) Method to deactivate user
    @Override
    public ResponsePojo<User> deleteUserDetail(UserDto userDto){

        Optional<User> userOptional = userRepository.findByAccountNumber(userDto.getAccountNumber());
        userOptional.orElseThrow(()-> new ApiException(ResponseUtils.USER_NOT_FOUND_MESSAGE));

        //The following are user updates
        User user = userOptional.get();
        user.setStatus(ResponseUtils.NON_ACTIVE);
        user.setModifiedAt(LocalDateTime.now());

        //updated information is saved to repository
        User savedUser = userRepository.save(user);

        String accountDetails = savedUser.getLastName() + "," + savedUser.getFirstName() + " " + savedUser.getOtherName()
                + " \n" + savedUser.getAccountNumber() + "\n" + savedUser.getAccountBalance();

        //Creating an object of EmailDetail
        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(userDto.getEmail())
                .emailSubject("ACCOUNT DE-ACTIVATION")
                .emailBody(String.format("Dear %s %s, your account has been deactivated due to inactivity of the account exceeding " +
                        "the minimum inactivity period of 5 years ." +
                        "\n You may visit any of our branch for more enquiry." +
                        "\n thanks for banking with us" +
                        "\n Akinov Bank \n" +
                                "\n Account Details:\n" +
                        accountDetails, userDto.getLastName(), userDto.getFirstName()))
                .build();
        //Send email to user
        emailService.sendSimpleEmail(emailDetail);
        //response is given back to the caller
        ResponsePojo<User> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.SUCCESS);
        responsePojo.setMessage(ResponseUtils.SUCCESS_MESSAGE);
        responsePojo.setData(user);

        return responsePojo;
    }
}

