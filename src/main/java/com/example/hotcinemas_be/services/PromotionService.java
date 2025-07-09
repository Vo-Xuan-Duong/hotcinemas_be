package com.example.hotcinemas_be.services;


import com.example.hotcinemas_be.dtos.requests.PromotionRequest;
import com.example.hotcinemas_be.dtos.responses.PromotionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
    public PromotionResponse createPromotion(PromotionRequest promotionRequest);
    public PromotionResponse getPromotionById(Long id);
    public PromotionResponse updatePromotion(Long id, PromotionRequest promotionRequest);
    public void deletePromotion(Long id);
    public PromotionResponse getPromotionByCode(String code);
    public Page<PromotionResponse> getAllPromotions(Pageable pageable);
    public boolean activatePromotion(Long id);
    public boolean deactivatePromotion(Long id);
    public Page<PromotionResponse> getActivePromotions(Pageable pageable);
}
