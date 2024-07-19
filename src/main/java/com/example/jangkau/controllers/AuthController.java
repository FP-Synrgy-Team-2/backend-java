package com.example.jangkau.controllers;

import com.example.jangkau.dto.auth.*;
import com.example.jangkau.dto.base.BaseResponse;
import com.example.jangkau.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.register(request), "Success Register User"));
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.login(request), "Success Login User"));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(Principal principal) {
        authService.logout(principal);
        return ResponseEntity.ok(BaseResponse.success(null, "Success Logout User"));
    }

    @PostMapping("/otp")
    public ResponseEntity<?> sendEmailOtp(@RequestBody EmailRequest req) {
        return ResponseEntity.ok(BaseResponse.success(authService.sendEmailOtp(req, "Register"), "Success Send OTP"));
    }

    @GetMapping("/otp/{token}")
    public ResponseEntity<?> confirmOtp(@PathVariable(value = "token") String tokenOtp) {
        return ResponseEntity.ok(BaseResponse.success(authService.confirmOtp(tokenOtp), "Success Send OTP"));
    }

    @PostMapping("/password")
    public ResponseEntity<?> sendEmailForgetPassword(@RequestBody EmailRequest req) {
        return ResponseEntity.ok(BaseResponse.success(authService.sendEmailOtp(req, "Forget Password"), "Success Send OTP Forget Password"));
    }

    @PostMapping("/password/otp")
    public ResponseEntity<?> checkValidOtp(@RequestBody OtpRequest otp) {
        return ResponseEntity.ok(BaseResponse.success(authService.checkOtpValid(otp), "Success Validate OTP"));
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.resetPassword(request), "Success Reset Password"));
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(authService.getCurrentUser(principal), "Success Get Current User Login"));
    }
}