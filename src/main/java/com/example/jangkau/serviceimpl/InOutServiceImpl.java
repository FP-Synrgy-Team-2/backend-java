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
    public static String AMOUNT = "amount";
    public static String SOURCE_ACCOUNT = "source-account";

    @Override
    public Map<String, List<?>> getIncome(Account account, Date startDate, Date endDate) {
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

        Map<String, List<?>> incomeDetails = new LinkedHashMap<>();
        incomeDetails.put(AMOUNT, transactionInAmount);
        incomeDetails.put(SOURCE_ACCOUNT, sourceInFrom);
        return incomeDetails;
    }

    @Override
    public void getOutcome(Account account, Date startDate, Date endDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
