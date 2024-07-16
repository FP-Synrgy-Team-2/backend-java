package com.example.jangkau.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiContract {

    @GetMapping("/api-contract")
    public String getApiContract(Model model) {
        return "api-contract";
    }
}

