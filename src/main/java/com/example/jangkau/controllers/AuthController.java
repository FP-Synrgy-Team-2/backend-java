package com.example.jangkau.controllers;

import com.example.jangkau.dto.auth.*;
import com.example.jangkau.dto.base.BaseResponse;
import com.example.jangkau.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenStore tokenStore;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.register(request), "Success Register User"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request, response);
        return ResponseEntity.ok(loginResponse);
    }

//    @PostMapping("login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok(BaseResponse.success(authService.login(request), "Success Login User"));
//    }

//    @PostMapping("/logout")
//    public void logout(HttpServletRequest request, Authentication authentication) {
//        String token = null;
//        Enumeration<String> headers = request.getHeaders("Authorization");
//        while (headers.hasMoreElements()) {
//            String value = headers.nextElement();
//            if ((value.toLowerCase().startsWith("bearer"))) {
//                token = value.substring("bearer".length()).trim();
//                OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
//                if (oAuth2AccessToken != null) {
//                    tokenStore.removeAccessToken(oAuth2AccessToken);
//                }
//            }
//        }
//    }

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