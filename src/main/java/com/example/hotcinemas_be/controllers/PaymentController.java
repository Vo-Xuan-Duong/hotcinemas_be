package com.example.hotcinemas_be.controllers;

import com.example.hotcinemas_be.dtos.common.ResponseData;
import com.example.hotcinemas_be.dtos.payment.requests.PaymentRequest;
import com.example.hotcinemas_be.dtos.payment.responses.PaymentResponse;
import com.example.hotcinemas_be.enums.PaymentStatus;
import com.example.hotcinemas_be.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment Management", description = "APIs for managing payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Create a new payment", description = "This endpoint allows creating a new payment for a booking.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PostMapping
    public ResponseEntity<ResponseData<PaymentResponse>> createPayment(
            @Valid @RequestBody PaymentRequest paymentRequest) {
        try {
            log.info("Creating new payment for booking ID: {}", paymentRequest.getBookingId());
            PaymentResponse paymentResponse = paymentService.createPayment(paymentRequest);

            ResponseData<PaymentResponse> responseData = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Payment has been successfully created")
                    .data(paymentResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
        } catch (Exception ex) {
            log.error("Error creating payment: {}", ex.getMessage());
            ResponseData<PaymentResponse> errorResponse = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Get all payments", description = "This endpoint retrieves all payments with pagination.")
    @GetMapping
    public ResponseEntity<ResponseData<Page<PaymentResponse>>> getAllPayments(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        try {
            log.info("Retrieving all payments with pagination");
            Page<PaymentResponse> payments = paymentService.getAllPayments(pageable);

            ResponseData<Page<PaymentResponse>> responseData = ResponseData.<Page<PaymentResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Payments retrieved successfully")
                    .data(payments)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving payments: {}", ex.getMessage());
            ResponseData<Page<PaymentResponse>> errorResponse = ResponseData.<Page<PaymentResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving payments: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get all payments (no pagination)", description = "This endpoint retrieves all payments without pagination.")
    @GetMapping("/all")
    public ResponseEntity<ResponseData<List<PaymentResponse>>> getAllPaymentsList() {
        try {
            log.info("Retrieving all payments without pagination");
            List<PaymentResponse> payments = paymentService.getAllPayments();

            ResponseData<List<PaymentResponse>> responseData = ResponseData.<List<PaymentResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Payments retrieved successfully")
                    .data(payments)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving payments: {}", ex.getMessage());
            ResponseData<List<PaymentResponse>> errorResponse = ResponseData.<List<PaymentResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving payments: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get a payment by ID", description = "This endpoint retrieves a payment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<PaymentResponse>> getPaymentById(
            @Parameter(description = "Payment ID") @PathVariable Long id) {
        try {
            log.info("Retrieving payment with ID: {}", id);
            PaymentResponse payment = paymentService.getPaymentById(id);

            ResponseData<PaymentResponse> responseData = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Payment retrieved successfully")
                    .data(payment)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving payment with ID {}: {}", id, ex.getMessage());
            ResponseData<PaymentResponse> errorResponse = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Get payments by booking ID", description = "This endpoint retrieves all payments for a specific booking.")
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ResponseData<List<PaymentResponse>>> getPaymentsByBookingId(
            @Parameter(description = "Booking ID") @PathVariable Long bookingId) {
        try {
            log.info("Retrieving payments for booking ID: {}", bookingId);
            List<PaymentResponse> payments = paymentService.getPaymentsByBookingId(bookingId);

            ResponseData<List<PaymentResponse>> responseData = ResponseData.<List<PaymentResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Payments for booking retrieved successfully")
                    .data(payments)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving payments for booking {}: {}", bookingId, ex.getMessage());
            ResponseData<List<PaymentResponse>> errorResponse = ResponseData.<List<PaymentResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving payments: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get payments by status", description = "This endpoint retrieves all payments with a specific status.")
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseData<List<PaymentResponse>>> getPaymentsByStatus(
            @Parameter(description = "Payment status") @PathVariable PaymentStatus status) {
        try {
            log.info("Retrieving payments with status: {}", status);
            List<PaymentResponse> payments = paymentService.getPaymentsByStatus(status);

            ResponseData<List<PaymentResponse>> responseData = ResponseData.<List<PaymentResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Payments with status retrieved successfully")
                    .data(payments)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving payments with status {}: {}", status, ex.getMessage());
            ResponseData<List<PaymentResponse>> errorResponse = ResponseData.<List<PaymentResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving payments: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Update a payment", description = "This endpoint allows updating an existing payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<PaymentResponse>> updatePayment(
            @Parameter(description = "Payment ID") @PathVariable Long id,
            @Valid @RequestBody PaymentRequest paymentRequest) {
        try {
            log.info("Updating payment with ID: {}", id);
            PaymentResponse paymentResponse = paymentService.updatePayment(id, paymentRequest);

            ResponseData<PaymentResponse> responseData = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Payment has been successfully updated")
                    .data(paymentResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error updating payment with ID {}: {}", id, ex.getMessage());
            ResponseData<PaymentResponse> errorResponse = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Partially update a payment", description = "This endpoint allows partially updating an existing payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment partially updated successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData<PaymentResponse>> partialUpdatePayment(
            @Parameter(description = "Payment ID") @PathVariable Long id,
            @RequestBody PaymentRequest paymentRequest) {
        try {
            log.info("Partially updating payment with ID: {}", id);
            PaymentResponse paymentResponse = paymentService.updatePayment(id, paymentRequest);

            ResponseData<PaymentResponse> responseData = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Payment has been partially updated")
                    .data(paymentResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error partially updating payment with ID {}: {}", id, ex.getMessage());
            ResponseData<PaymentResponse> errorResponse = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Update payment status", description = "This endpoint allows updating the status of a payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseData<PaymentResponse>> updatePaymentStatus(
            @Parameter(description = "Payment ID") @PathVariable Long id,
            @RequestBody PaymentStatus status) {
        try {
            log.info("Updating payment status for ID {} to {}", id, status);
            PaymentResponse paymentResponse = paymentService.updatePaymentStatus(id, status);

            ResponseData<PaymentResponse> responseData = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Payment status has been successfully updated")
                    .data(paymentResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error updating payment status for ID {}: {}", id, ex.getMessage());
            ResponseData<PaymentResponse> errorResponse = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Update transaction ID", description = "This endpoint allows updating the transaction ID of a payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction ID updated successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PatchMapping("/{id}/transaction-id")
    public ResponseEntity<ResponseData<PaymentResponse>> updateTransactionId(
            @Parameter(description = "Payment ID") @PathVariable Long id,
            @RequestBody String transactionId) {
        try {
            log.info("Updating transaction ID for payment ID {} to {}", id, transactionId);
            PaymentResponse paymentResponse = paymentService.updateTransactionId(id, transactionId);

            ResponseData<PaymentResponse> responseData = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Transaction ID has been successfully updated")
                    .data(paymentResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error updating transaction ID for payment ID {}: {}", id, ex.getMessage());
            ResponseData<PaymentResponse> errorResponse = ResponseData.<PaymentResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Delete a payment", description = "This endpoint allows deleting a payment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deletePayment(
            @Parameter(description = "Payment ID") @PathVariable Long id) {
        try {
            log.info("Deleting payment with ID: {}", id);
            paymentService.deletePayment(id);

            ResponseData<Void> responseData = ResponseData.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("Payment has been successfully deleted")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error deleting payment with ID {}: {}", id, ex.getMessage());
            ResponseData<Void> errorResponse = ResponseData.<Void>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
