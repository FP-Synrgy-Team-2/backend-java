package com.example.jangkau.serviceimpl;

import com.example.jangkau.dto.TransactionInOutHistoryDTO;
import com.example.jangkau.dto.TransactionsHistoryDTO;
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
    public static String TARGET_ACCOUNT = "target-account";

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Map<String, List<?>> getIncome(Account account, Date startDate, Date endDate) {
        List<Transactions> transactionsList = transactionRepository.findByBeneficiaryAccount(account);
        List<Transactions> transactionsInAPeriod = transactionsList.stream()
                .filter(transactions -> {
                    boolean a = transactions.getTransactionDate().after(startDate);
                    boolean b = transactions.getTransactionDate().before(endDate);
                    return a && b;
                })
                .collect(Collectors.toList());
        List<Double> transactionInAmount = transactionsInAPeriod.stream()
                .map(Transactions::getAmount)
                .collect(Collectors.toList());
        List<Account> sourceAccount = transactionsInAPeriod.stream()
                .map(Transactions::getAccountId)
                .collect(Collectors.toList());
        Map<String, List<?>> incomeDetails = new LinkedHashMap<>();
        incomeDetails.put(AMOUNT, transactionInAmount);
        incomeDetails.put(SOURCE_ACCOUNT, sourceAccount);
        return incomeDetails;
    }

    @Override
    public Map<String, List<?>> getOutcome(Account account, Date startDate, Date endDate) {
        List<Transactions> transactionsList = transactionRepository.findByAccountId(account);
        List<Transactions> transactionsInAPeriod = transactionsList.stream()
                .filter(transactions -> {
                    boolean a = transactions.getTransactionDate().after(startDate);
                    boolean b = transactions.getTransactionDate().before(endDate);
                    return a && b;
                })
                .collect(Collectors.toList());
        List<Double> transactionInAmount = transactionsInAPeriod.stream()
                .map(Transactions::getAmount)
                .collect(Collectors.toList());
        List<Account> recipientAccount = transactionsInAPeriod.stream()
                .map(Transactions::getBeneficiaryAccount)
                .collect(Collectors.toList());
        Map<String, List<?>> outcomeDetails = new LinkedHashMap<>();
        outcomeDetails.put(AMOUNT, transactionInAmount);
        outcomeDetails.put(TARGET_ACCOUNT, recipientAccount);
        return outcomeDetails;
    }

    @Override
    public List<TransactionInOutHistoryDTO> getTransactionInOutHistory(Account account) {
        List<TransactionInOutHistoryDTO> transactionInOutHistoryDTOList = new ArrayList<>();
        List<Transactions> transactions = transactionRepository.findAllTransactionsHistoryByAccount(account.getId());
        transactions.forEach(
                transaction -> {
                    TransactionInOutHistoryDTO transactionInOutHistoryDTO = TransactionInOutHistoryDTO.builder()
                            .transactionId(transaction.getTransactionId())
                            .transactionDate(transaction.getTransactionDate())
                            .note(transaction.getNote())
                            .build();
                    if (transaction.getAccountId() == account) {
                        transactionInOutHistoryDTO.setAmount("- "+transaction.getAmount());
                        transactionInOutHistoryDTO.setSourceId(account.getId());
                        transactionInOutHistoryDTO.setBeneficiaryId(transaction.getBeneficiaryAccount().getId());
                    } else if (transaction.getBeneficiaryAccount() == account) {
                        transactionInOutHistoryDTO.setAmount("+ "+transaction.getAmount());
                        transactionInOutHistoryDTO.setSourceId(transaction.getAccountId().getId());
                        transactionInOutHistoryDTO.setBeneficiaryId(account.getId());
                    }
                    transactionInOutHistoryDTOList.add(transactionInOutHistoryDTO);
                }
        );
        return transactionInOutHistoryDTOList;
    }
}
