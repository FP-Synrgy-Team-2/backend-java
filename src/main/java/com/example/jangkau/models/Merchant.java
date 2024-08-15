package com.example.jangkau.models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;
import javax.persistence.CascadeType;
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
@Table(name = "merchant")
@SQLDelete(sql = "update merchant set deleted_date = now() where id =?")
@Where(clause = "deleted_date is null")
public class Merchant extends BaseDate{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;


}
