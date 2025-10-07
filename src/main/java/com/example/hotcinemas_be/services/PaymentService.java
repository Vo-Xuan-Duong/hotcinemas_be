package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.payment.requests.PaymentRequest;
import com.example.hotcinemas_be.dtos.payment.responses.PaymentResponse;
import com.example.hotcinemas_be.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse updatePayment(Long paymentId, PaymentRequest paymentRequest);

    void deletePayment(Long paymentId);

    Page<PaymentResponse> getAllPayments(Pageable pageable);

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getPaymentsByBookingId(Long bookingId);

    List<PaymentResponse> getPaymentsByStatus(PaymentStatus status);

    PaymentResponse updatePaymentStatus(Long paymentId, PaymentStatus status);

    PaymentResponse updateTransactionId(Long paymentId, String transactionId);
}
