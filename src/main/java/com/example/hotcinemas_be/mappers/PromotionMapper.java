package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.promotion.responses.PromotionResponse;
import com.example.hotcinemas_be.models.Promotion;
import org.springframework.stereotype.Service;

@Service
public class PromotionMapper {
    public PromotionResponse mapToResponse(Promotion promotion) {
        if (promotion == null) {
            return null;
        }

        PromotionResponse response = new PromotionResponse();
        response.setId(promotion.getId());
        response.setCode(promotion.getCode());
        response.setDescription(promotion.getDescription());
        response.setQuantity(promotion.getQuantity());
        response.setDiscountValue(promotion.getDiscountValue());
        response.setStartDate(promotion.getStartDate());
        response.setEndDate(promotion.getEndDate());
        response.setMinOrderAmount(promotion.getMinOrderAmount());
        response.setMaxDiscountAmount(promotion.getMaxDiscountAmount());
        response.setActive(promotion.getIsActive());

        return response;
    }
}
