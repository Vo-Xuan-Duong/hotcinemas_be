package com.example.hotcinemas_be.dtos.promotion.requests;

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
public class PromotionRequest {
    private String code;
    private String description;
    private DiscountType discountType;
    private Integer quantity;
    private Double discountValue;
    private Double minOrderAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

