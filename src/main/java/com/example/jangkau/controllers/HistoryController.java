package com.example.jangkau.controllers;

import com.example.jangkau.dto.AccountResponseDTO;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.User;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.repositories.UserRepository;
import com.example.jangkau.serviceimpl.InOutServiceImpl;
import com.example.jangkau.services.InOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    InOutService inOutService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/income/{username}")
    public ResponseEntity<?> income(@PathVariable("username") String username, @RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate) {
        User user = userRepository.findByUsername(username);
        Account account = accountRepository.findByUser(user).orElse(null);
        if (account == null) return ResponseEntity.notFound().build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date formerDate;
        Date latterDate;
        try {
            formerDate = sdf.parse(startDate);
            latterDate = sdf.parse(endDate);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }

        Map<String, List<?>> income = inOutService.getIncome(account, formerDate, latterDate);
        List<Double> incomeAmount = (List<Double>) income.get(InOutServiceImpl.AMOUNT);
        List<Account> incomeSource = (List<Account>) income.get(InOutServiceImpl.SOURCE_ACCOUNT);
        Map<String, Object> data = new HashMap<>();
        List<AccountResponseDTO> incomeSourceDto = incomeSource.stream().map(
                account1 -> {
                    AccountResponseDTO accountResponseDTO = new AccountResponseDTO(
                            account1.getId(),
                            account1.getOwnerName(),
                            account1.getAccountNumber(),
                            account1.getBalance()
                    );
                    return accountResponseDTO;
                }
        ).collect(Collectors.toList());
        data.put("income_amount", incomeAmount);
        data.put("income_source", incomeSourceDto);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/outcome/{username}")
    public ResponseEntity<?> outcome(@PathVariable("username") String username, @RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate) {
        User user = userRepository.findByUsername(username);
        Account account = accountRepository.findByUser(user).orElse(null);
        if (account == null) return ResponseEntity.notFound().build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date formerDate;
        Date latterDate;
        try {
            formerDate = sdf.parse(startDate);
            latterDate = sdf.parse(endDate);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }

        Map<String, List<?>> outcome = inOutService.getOutcome(account, formerDate, latterDate);
        List<Double> outcomeAmount = (List<Double>) outcome.get(InOutServiceImpl.AMOUNT);
        List<Account> outcomeTarget = (List<Account>) outcome.get(InOutServiceImpl.TARGET_ACCOUNT);
        Map<String, List<?>> data = new HashMap<>();
        List<AccountResponseDTO> outcomeTargetDto = outcomeTarget.stream().map(
                account1 -> {
                    AccountResponseDTO accountResponseDTO = new AccountResponseDTO(
                            account1.getId(),
                            account1.getOwnerName(),
                            account1.getAccountNumber(),
                            account1.getBalance()
                    );
                    return accountResponseDTO;
                }
        ).collect(Collectors.toList());
        data.put("outcome_amount", outcomeAmount);
        data.put("outcome_target", outcomeTargetDto);
        return ResponseEntity.ok(data);
    }
}
