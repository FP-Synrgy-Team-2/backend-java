package com.example.jangkau.serviceimpl;

import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.repositories.TransactionRepository;
import com.example.jangkau.services.InOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InOutServiceImpl implements InOutService {

    @Override
    public void getIncome(Account account, Date startDate, Date endDate) {
//        throw new UnsupportedOperationException("Not supported yet.");
        List<Transactions> transactionsList = account.getTransactionsFrom();
        List<Transactions> transactionsInAPeriod = transactionsList.stream()
                .filter(transactions -> {
                    boolean a = transactions.getTransactionDate().after(startDate);
                    boolean b = transactions.getTransactionDate().before(endDate);
                    return a || b;
                })
                .collect(Collectors.toList());
        List<Double> transactionInAmount = transactionsInAPeriod.stream()
                .map(Transactions::getAmount)
                .collect(Collectors.toList());
        List<Account> sourceInFrom = transactionsInAPeriod.stream()
                .map(Transactions::getAccountId)
                .collect(Collectors.toList());
        List<String[]> incomeHistory = new ArrayList<>();
    }

    @Override
    public void getOutcome(Account account, Date startDate, Date endDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
