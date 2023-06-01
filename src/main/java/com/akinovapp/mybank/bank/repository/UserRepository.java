package com.akinovapp.mybank.bank.repository;

import com.akinovapp.mybank.bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Spliterator;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByAccountNumber(String accountNumber);
    Boolean existsByEmail(String email);
    Optional<User> findByAccountNumber(String accountNumber);

}

