package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.responses.PromotionResponse;
import com.example.hotcinemas_be.models.Promotion;
import org.springframework.stereotype.Service;

@Service
public class PromotionMapper {
    public PromotionResponse mapToResponse(Promotion promotion) {
        if (promotion == null) {
            return null;
        }

        PromotionResponse response = new PromotionResponse();
        response.setPromotionId(promotion.getPromotionId());
        response.setCode(promotion.getCode());
        response.setDescription(promotion.getDescription());
        response.setDiscountType(promotion.getDiscountType());
        response.setQuantity(promotion.getQuantity());
        response.setDiscountValue(promotion.getDiscountValue());
        response.setStartDate(promotion.getStartDate());
        response.setEndDate(promotion.getEndDate());

        return response;
    }
}
