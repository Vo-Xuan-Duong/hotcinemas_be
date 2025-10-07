package com.example.hotcinemas_be.models;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.hotcinemas_be.enums.DiscountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue; // DECIMAL(5,2) in DB, Double in Java

    @Column(name = "quantity") // Nullable, Integer in DB
    private Integer quantity; // Nullable, Integer in Java

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "min_order_amount", nullable = false)
    private BigDecimal minOrderAmount ; // DECIMAL(10,2) in DB, Double in Java

    @Column(name = "max_discount_amount")
    private BigDecimal maxDiscountAmount; // Nullable, DECIMAL(10,2) in DB, Double in Java

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Relationships
    @Builder.Default
    @ManyToMany(mappedBy = "promotions")
    private List<Booking> bookings = new ArrayList<>();
}
