package com.example.jangkau.scheduler;

import java.util.concurrent.ExecutionException;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.jangkau.services.QrisService;

@Component
public class CleanQRScheduler {
    @Autowired QrisService qrisService;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void cronJob() throws ExecutionException, InterruptedException {
        qrisService.cleanData();
    }

}
