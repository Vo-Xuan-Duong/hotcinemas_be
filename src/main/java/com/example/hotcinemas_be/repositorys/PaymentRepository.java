package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.enums.PaymentStatus;
import com.example.hotcinemas_be.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByBookingId(Long bookingId);

    List<Payment> findByStatus(PaymentStatus status);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByBookingIdAndStatus(Long bookingId, PaymentStatus status);
}
