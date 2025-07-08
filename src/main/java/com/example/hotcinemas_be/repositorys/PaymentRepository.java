package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
