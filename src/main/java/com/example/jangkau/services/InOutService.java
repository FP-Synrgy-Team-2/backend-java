package com.example.jangkau.services;

import com.example.jangkau.models.Account;

import java.util.Date;

public interface InOutService {
    void getIncome(Account account, Date startDate, Date endDate);

    void getOutcome(Account account, Date startDate, Date endDate);
}
