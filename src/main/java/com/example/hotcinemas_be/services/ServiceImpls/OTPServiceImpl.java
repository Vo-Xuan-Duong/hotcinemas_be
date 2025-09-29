package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.services.OTPService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OTPServiceImpl implements OTPService {

    private final RedisTemplate<String, String> redisTemplate;

    public OTPServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private void saveOTPToRedis(String email, String otp) {
        String redisKey = "otp:" + email;
        redisTemplate.opsForValue().set(redisKey, otp, 15, TimeUnit.MINUTES);
    }

    private void removeOTPFromRedis(String email) {
        String redisKey = "otp:" + email;
        redisTemplate.delete(redisKey);
    }

    @Override
    public String generateOTP(String email) {
        SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000);
        String otpString = String.format("%06d", otp);
        saveOTPToRedis(email, otpString);
        return otpString;
    }

    @Override
    public boolean validateOTP(String email, String otp) {
        String redisKey = "otp:" + email;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);
        if (storedOtp != null && storedOtp.equals(otp)) {
            removeOTPFromRedis(email);
            return true;
        }
        return false;
    }
}
