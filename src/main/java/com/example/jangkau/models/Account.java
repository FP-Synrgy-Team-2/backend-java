package com.example.jangkau.models;

import com.example.jangkau.utils.NumberGeneratorUtil;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import com.example.jangkau.models.base.BaseDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account extends BaseDate {
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
    
    @OneToMany(mappedBy = "accountId", cascade = CascadeType.ALL)
    private List<Transactions> transactionsFrom;

    @OneToMany(mappedBy = "beneficiaryAccount", cascade = CascadeType.ALL)
    private List<Transactions> transactionsTo;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<SavedAccounts> savedAccounts;

    private String pin;

    @OneToOne(mappedBy = "account")
    private Merchant merchant;

    @PrePersist
    public void generateAccountNumber() {
        if (this.accountNumber == null || this.accountNumber.isEmpty()) {
            this.accountNumber = NumberGeneratorUtil.generateNumber(12);
        }
    }

    public void setPin(Integer pin, PasswordEncoder passwordEncoder) {
        this.pin = passwordEncoder.encode(pin.toString());
    }
}
