package com.example.jangkau.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    private UUID id;

    @Column(name = "account_name")
    private String accountNumber;

    @Column(name = "balance")
    private Double balance;

    @OneToOne(targetEntity = User.class)
    private User user;
}
