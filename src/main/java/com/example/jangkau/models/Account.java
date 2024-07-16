package com.example.jangkau.models;

import com.example.jangkau.utils.NumberGeneratorUtil;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.example.jangkau.models.base.BaseDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
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

    @Column(name = "pin")
    private String pin;

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
