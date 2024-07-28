package com.example.jangkau.controllers;

import com.example.jangkau.dto.base.BaseResponse;
import com.example.jangkau.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

//@Tag(name = "Logout")
//@RestController
//@RequestMapping("/logout")
//public class LogoutController {
//
////    @Autowired
////    private AuthService authService;
//
////    @PutMapping()
////    public ResponseEntity<?> logout(Principal principal) {
////        authService.logout(principal);
////        return ResponseEntity.ok(BaseResponse.success(null, "Logout Successful"));
////    }
////}
