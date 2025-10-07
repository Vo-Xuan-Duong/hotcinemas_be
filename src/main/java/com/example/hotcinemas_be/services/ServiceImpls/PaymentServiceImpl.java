package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.payment.requests.PaymentRequest;
import com.example.hotcinemas_be.dtos.payment.responses.PaymentResponse;
import com.example.hotcinemas_be.enums.PaymentStatus;
import com.example.hotcinemas_be.mappers.PaymentMapper;
import com.example.hotcinemas_be.models.Booking;
import com.example.hotcinemas_be.models.Payment;
import com.example.hotcinemas_be.repositorys.BookingRepository;
import com.example.hotcinemas_be.repositorys.PaymentRepository;
import com.example.hotcinemas_be.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final BookingRepository bookingRepository;

    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        // Validate booking exists
        Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + paymentRequest.getBookingId()));

        // Generate transaction ID if not provided
        String transactionId = paymentRequest.getTransactionId();
        if (transactionId == null || transactionId.trim().isEmpty()) {
            transactionId = "TXN_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        }

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(paymentRequest.getAmount())
                .paymentMethod(paymentRequest.getPaymentMethod())
                .transactionId(transactionId)
                .status(paymentRequest.getStatus() != null ? paymentRequest.getStatus() : PaymentStatus.PENDING)
                .currency(paymentRequest.getCurrency() != null ? paymentRequest.getCurrency() : "VND")
                .notes(paymentRequest.getNotes())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.mapToResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
        return paymentMapper.mapToResponse(payment);
    }

    @Override
    public PaymentResponse updatePayment(Long paymentId, PaymentRequest paymentRequest) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        // Update booking if provided
        if (paymentRequest.getBookingId() != null) {
            Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
                    .orElseThrow(
                            () -> new RuntimeException("Booking not found with id: " + paymentRequest.getBookingId()));
            payment.setBooking(booking);
        }

        // Update other fields
        if (paymentRequest.getAmount() != null) {
            payment.setAmount(paymentRequest.getAmount());
        }
        if (paymentRequest.getPaymentMethod() != null) {
            payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        }
        if (paymentRequest.getTransactionId() != null) {
            payment.setTransactionId(paymentRequest.getTransactionId());
        }
        if (paymentRequest.getStatus() != null) {
            payment.setStatus(paymentRequest.getStatus());
        }
        if (paymentRequest.getCurrency() != null) {
            payment.setCurrency(paymentRequest.getCurrency());
        }
        if (paymentRequest.getNotes() != null) {
            payment.setNotes(paymentRequest.getNotes());
        }

        Payment updatedPayment = paymentRepository.save(payment);
        return paymentMapper.mapToResponse(updatedPayment);
    }

    @Override
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
        paymentRepository.delete(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAllPayments(Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(paymentMapper::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(paymentMapper::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByBookingId(Long bookingId) {
        List<Payment> payments = paymentRepository.findByBookingId(bookingId);
        return payments.stream().map(paymentMapper::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByStatus(status);
        return payments.stream().map(paymentMapper::mapToResponse).toList();
    }

    @Override
    public PaymentResponse updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        payment.setStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);
        return paymentMapper.mapToResponse(updatedPayment);
    }

    @Override
    public PaymentResponse updateTransactionId(Long paymentId, String transactionId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        payment.setTransactionId(transactionId);
        Payment updatedPayment = paymentRepository.save(payment);
        return paymentMapper.mapToResponse(updatedPayment);
    }
}
