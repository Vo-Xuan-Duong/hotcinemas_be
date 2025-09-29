package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void sendOTPConfirmationEmail(String email, String otp) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("OTP Confirmation");
            String content = "<html><body>" +
                    "<h2>OTP Confirmation</h2>" +
                    "<p>Your OTP is: <strong>" + otp + "</strong></p>" +
                    "<p>Please use this OTP to complete your registration.</p>" +
                    "</body></html>";
            helper.setText(content, true); // true indicates HTML content
            helper.setFrom("duongvo.261104@gmail.com"); // Set your email address here
            javaMailSender.send(mimeMessage);

        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP confirmation email: " + e.getMessage());
        }
    }
}
