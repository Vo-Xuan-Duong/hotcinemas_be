package com.example.hotcinemas_be.services;

public interface OTPService {
    public String generateOTP(String email);
    public boolean validateOTP(String email, String otp);
}
