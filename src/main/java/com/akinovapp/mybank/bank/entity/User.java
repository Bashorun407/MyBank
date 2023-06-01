package com.akinovapp.mybank.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "my_bank")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "other_name")
    private String otherName;
    @Column(name = "gender")
    private String gender;
    @Column(name = "home_address")
    private String home_address;
    @Column(name = "state_of_origin")
    private String stateOfOrigin;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "account_balance")
    private BigDecimal accountBalance;
    @Column(name = "email_address")
    private String email;
    @Column(name = "mobile_number")
    private String phoneNumber;
    @Column(name = "alternative_number")
    private String alternativeNumber;
    @Column(name = "active_status")
    private String status;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @CreationTimestamp
    @Column(name = "date_time_created")
    private LocalDateTime createdAt;
    @CreationTimestamp
    @Column(name = "date_time_modified")
    private LocalDateTime modifiedAt;

}
