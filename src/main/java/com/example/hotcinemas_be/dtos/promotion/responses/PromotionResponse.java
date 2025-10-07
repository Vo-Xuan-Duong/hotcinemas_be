package com.example.hotcinemas_be.dtos.promotion.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.hotcinemas_be.enums.DiscountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResponse {
    private Long id;
    private String code;
    private String description;
    private Integer quantity;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal maxDiscountAmount;
    private boolean isActive;
}

