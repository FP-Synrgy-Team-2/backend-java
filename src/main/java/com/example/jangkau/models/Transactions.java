package com.example.jangkau.models;


import java.util.Date;
import java.util.UUID;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;

import com.example.jangkau.models.base.BaseDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private UUID transaction_id;

    @Column(nullable = false)
    private double amount;

    private double admin_fee = 0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date", updatable = false)
    @CreatedDate
    private Date transaction_date;

    private String note;
    
    private boolean is_saved;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "account_id")
    private Account account_id;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "beneficiary_account")
    private Account beneficiary_account;

}
