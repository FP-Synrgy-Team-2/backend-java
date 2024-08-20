package com.example.jangkau.models;

import com.example.jangkau.utils.NumberGeneratorUtil;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.example.jangkau.models.base.BaseDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.SQLDelete;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account_qr")
@SQLDelete(sql = "update account_qr set deleted_date = now() where id =?")
@Where(clause = "deleted_date is null")
public class AccountQR extends BaseDate {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @Column(name = "qr")
    private String qr;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "accountId")
    private Account accountQr;

    private LocalDateTime expiredTime;
}
