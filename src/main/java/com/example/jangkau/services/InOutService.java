package com.example.jangkau.services;

import com.example.jangkau.models.Account;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface InOutService {
    Map<String, List<?>> getIncome(Account account, Date startDate, Date endDate);

    void getOutcome(Account account, Date startDate, Date endDate);
}
