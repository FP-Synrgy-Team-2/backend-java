package com.example.jangkau.models;


import java.util.Date;
import java.util.UUID;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;

import com.example.jangkau.models.base.BaseDate;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
@SQLDelete(sql = "update transactions set deleted_date = now() where transaction_id =?")
@Where(clause = "deleted_date is null")
public class Transactions extends BaseDate{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID transactionId;

    @Column(nullable = false)
    private double amount;

    private double adminFee = 0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transactionDate", updatable = false)
    @CreatedDate
    private Date transactionDate;

    private String note;
    
    private boolean isSaved;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "accountId")
    private Account accountId;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "beneficiaryAccount")
    private Account beneficiaryAccount;

}
