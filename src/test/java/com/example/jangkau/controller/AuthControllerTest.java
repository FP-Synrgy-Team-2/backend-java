package com.example.jangkau.controller;

import com.example.jangkau.controllers.AuthController;
import com.example.jangkau.dto.auth.*;
import com.example.jangkau.dto.base.BaseResponse;
import com.example.jangkau.models.User;
import com.example.jangkau.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        // Prepare test data
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");

        // Prepare mock response (User object)
        User user = new User();
        user.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));  // Using a valid UUID
        user.setUsername("testuser");

        // Mock the service method
        when(authService.register(request)).thenReturn(user);

        // Call the controller method
        ResponseEntity<?> response = authController.register(request);

        // Verify and assert
        verify(authService, times(1)).register(request);
        assertEquals(200, response.getStatusCodeValue());

        BaseResponse<?> body = (BaseResponse<?>) response.getBody();
        assertEquals("Success Register User", body.getMessage());
        assertEquals(user, body.getData());
    }

    @Test
    void testLogin() {
        // Prepare test data
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");

        HttpServletResponse httpResponse = mock(HttpServletResponse.class);

        // Mock the service method
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken("dummy_token")
                .build();

        when(authService.login(request, httpResponse)).thenReturn(loginResponse);

        // Call the controller method
        ResponseEntity<BaseResponse<LoginResponse>> response = authController.login(request, httpResponse);

        // Verify and assert
        verify(authService, times(1)).login(request, httpResponse);
        assertEquals(200, response.getStatusCodeValue());

        BaseResponse<LoginResponse> body = response.getBody();
        assertEquals("Success Login User", body.getMessage());
        assertEquals("dummy_token", body.getData().getAccessToken());
    }

    @Test
    void testSendEmailOtp() {
        // Prepare test data
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmailAddress("test@example.com");

        // Mock the service method
        when(authService.sendEmailOtp(emailRequest, "Register")).thenReturn("OTP sent");

        // Call the controller method
        ResponseEntity<?> response = authController.sendEmailOtp(emailRequest);

        // Verify and assert
        verify(authService, times(1)).sendEmailOtp(emailRequest, "Register");
        assertEquals(200, response.getStatusCodeValue());

        BaseResponse<?> body = (BaseResponse<?>) response.getBody();
        assertEquals("Success Send OTP", body.getMessage());
        assertEquals("OTP sent", body.getData());
    }

    @Test
    void testCheckValidOtp() {
        // Prepare test data
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setOtp("123456");

        // Mock the service method
        when(authService.checkOtpValid(otpRequest)).thenReturn(true);

        // Call the controller method
        ResponseEntity<?> response = authController.checkValidOtp(otpRequest);

        // Verify and assert
        verify(authService, times(1)).checkOtpValid(otpRequest);
        assertEquals(200, response.getStatusCodeValue());

        BaseResponse<?> body = (BaseResponse<?>) response.getBody();
        assertEquals("Success Validate OTP", body.getMessage());
        assertEquals(true, body.getData());
    }

    @Test
    void testChangePassword() {
        // Prepare test data
        ResetPasswordRequest passwordRequest = new ResetPasswordRequest();
        passwordRequest.setNewPassword("newpass");

        // Mock the service method
        when(authService.resetPassword(passwordRequest)).thenReturn("Password changed");

        // Call the controller method
        ResponseEntity<?> response = authController.changePassword(passwordRequest);

        // Verify and assert
        verify(authService, times(1)).resetPassword(passwordRequest);
        assertEquals(200, response.getStatusCodeValue());

        BaseResponse<?> body = (BaseResponse<?>) response.getBody();
        assertEquals("Success Reset Password", body.getMessage());
        assertEquals("Password changed", body.getData());
    }

}