package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.requests.NewPassword;
import com.example.hotcinemas_be.dtos.requests.LoginRequest;
import com.example.hotcinemas_be.dtos.requests.RegisterRequest;
import com.example.hotcinemas_be.dtos.responses.AuthResponse;
import com.example.hotcinemas_be.models.User;


public interface AuthService {
    public AuthResponse loginHandler(LoginRequest loginRequest);
    public AuthResponse refreshTokenHandler(String refreshToken);
    public AuthResponse registerHandler(RegisterRequest registerRequest);
    public Boolean verifyOTP(String email, String otpCode);
    public void logoutHandler(String accessToken);
    public Boolean forgetPassword(String email);
    public Boolean verifyOTPChangePasswordToken(String email, String otpCode);
    public Boolean changePassword(NewPassword newPassword);
    public User currentUser();
}
