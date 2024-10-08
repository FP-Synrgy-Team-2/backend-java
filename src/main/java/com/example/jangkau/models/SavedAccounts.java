package com.example.jangkau.models;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import com.example.jangkau.models.base.BaseDate;

import javax.persistence.*;


import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "savedaccount")
public class SavedAccounts extends BaseDate {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "saved_account_id")
    private UUID id;


    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "account_id")
    private Account account;

}
