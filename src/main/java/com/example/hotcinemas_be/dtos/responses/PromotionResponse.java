package com.example.hotcinemas_be.dtos.responses;

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
    private Long promotionId;
    private String code;
    private String description;
    private DiscountType discountType; // Assuming it's a string representation of the enum
    private Integer quantity; // Assuming you want to return the quantity as well
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private LocalDateTime startDate; // Assuming you want to return it as a formatted string
    private LocalDateTime endDate; // Assuming you want to return it as a formatted string
    private BigDecimal maxDiscountAmount;
    private boolean isActive; // Assuming you want to indicate if the promotion is currently active

}
