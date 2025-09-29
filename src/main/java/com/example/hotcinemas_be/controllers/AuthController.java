package com.example.hotcinemas_be.controllers;

import com.example.hotcinemas_be.dtos.ResponseData;
import com.example.hotcinemas_be.dtos.requests.LoginRequest;
import com.example.hotcinemas_be.dtos.requests.NewPassword;
import com.example.hotcinemas_be.dtos.requests.RegisterRequest;
import com.example.hotcinemas_be.services.AuthService;
import com.example.hotcinemas_be.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login user", description = "Login user with email and password")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        ResponseData<?> responseData = ResponseData.builder()
                .status(200)
                .message("Successfully logged in")
                .data(authService.loginHandler(loginRequest))
                .build();
        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Refresh token", description = "Refresh JWT token using refresh token")
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        ResponseData<?> responseData = ResponseData.builder()
                .status(200)
                .message("Successfully refreshed token")
                .data(authService.refreshTokenHandler(refreshToken))
                .build();
        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Register user", description = "Register a new user with email and password")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        ResponseData<?> responseData = ResponseData.builder()
                .status(200)
                .message("Successfully registered")
                .data(authService.registerHandler(registerRequest))
                .build();
        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Verify OTP", description = "Verify OTP for user registration")
    @GetMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otpCode) {
        boolean isVerified = authService.verifyOTP(email, otpCode);
        ResponseData<?> responseData = ResponseData.builder()
                .status(isVerified ? 200 : 400)
                .message(isVerified ? "OTP verified successfully" : "Invalid OTP")
                .data(null)
                .build();
        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Get current user", description = "Get details of the currently logged-in user")
    @GetMapping("/current-user")
    public ResponseEntity<?> currentUser() {
        ResponseData<?> responseData = ResponseData.builder()
                .status(200)
                .message("Successfully retrieved current user")
                .data(authService.currentUser())
                .build();
        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Logout user", description = "Logout user by invalidating the access token")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        authService.logoutHandler(token);
        ResponseData<?> responseData = ResponseData.builder()
                .status(200)
                .message("Successfully logged out")
                .build();
        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Forget password", description = "Send OTP to email for password reset")
    @GetMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestParam String email) {
        ResponseData<?> responseData = ResponseData.builder()
                .status(200)
                .message("OTP sent to email for password reset")
                .data(authService.forgetPassword(email))
                .build();
        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Change password", description = "Change user password using OTP")
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody NewPassword newPassword) {
        boolean isChanged = authService.changePassword(newPassword);
        ResponseData<?> responseData = ResponseData.builder()
                .status(isChanged ? 200 : 400)
                .message(isChanged ? "Password changed successfully" : "Failed to change password")
                .data(null)
                .build();
        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Verify OTP for password change", description = "Verify OTP for changing password")
    @GetMapping("/verify-otp-change-password")
    public ResponseEntity<?> verifyOtpChangePassword(@RequestParam String email, @RequestParam String otpCode) {
        boolean isVerified = authService.verifyOTPChangePasswordToken(email, otpCode);
        ResponseData<?> responseData = ResponseData.builder()
                .status(isVerified ? 200 : 400)
                .message(isVerified ? "OTP verified successfully" : "Invalid OTP")
                .data(null)
                .build();
        return ResponseEntity.ok(responseData);
    }
}
