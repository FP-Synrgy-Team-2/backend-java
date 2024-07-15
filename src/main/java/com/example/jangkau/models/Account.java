package com.example.jangkau.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "account_id")
    private UUID id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "balance")
    private Double balance;

    @OneToOne(targetEntity = User.class)
    private User user;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "pin")
    private Integer pin;
}
