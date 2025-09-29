package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.models.Booking;

public interface EmailService {
    void sendOTPConfirmationEmail(String email, String otp);
}
