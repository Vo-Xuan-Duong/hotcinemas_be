package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.requests.PromotionRequest;
import com.example.hotcinemas_be.dtos.responses.PromotionResponse;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.PromotionMapper;
import com.example.hotcinemas_be.models.Promotion;
import com.example.hotcinemas_be.repositorys.PromotionRepository;
import com.example.hotcinemas_be.services.PromotionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public PromotionServiceImpl(PromotionRepository promotionRepository,
                                PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Override
    public PromotionResponse createPromotion(PromotionRequest promotionRequest) {
        Promotion promotion = new Promotion();
        promotion.setCode(promotionRequest.getCode());
        promotion.setDescription(promotionRequest.getDescription());
        promotion.setQuantity(promotionRequest.getQuantity());
        promotion.setStartDate(promotionRequest.getStartDate());
        promotion.setEndDate(promotionRequest.getEndDate());
        promotion.setDiscountType(promotionRequest.getDiscountType());
        promotion.setDiscountValue(BigDecimal.valueOf(promotionRequest.getDiscountValue()));

        return promotionMapper.mapToResponse(promotionRepository.save(promotion));
    }

    @Override
    public PromotionResponse getPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ErrorException("Promotion not found with id: " + id, ErrorCode.ERROR_MODEL_NOT_FOUND));
        return promotionMapper.mapToResponse(promotion);
    }

    @Override
    public PromotionResponse updatePromotion(Long id, PromotionRequest promotionRequest) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ErrorException("Promotion not found with id: " + id, ErrorCode.ERROR_MODEL_NOT_FOUND));
        promotion.setCode(promotionRequest.getCode());
        promotion.setDescription(promotionRequest.getDescription());
        promotion.setDiscountType(promotionRequest.getDiscountType());
        promotion.setQuantity(promotionRequest.getQuantity());
        promotion.setDiscountValue(BigDecimal.valueOf(promotionRequest.getDiscountValue()));
        promotion.setStartDate(promotionRequest.getStartDate());
        promotion.setEndDate(promotionRequest.getEndDate());

        return promotionMapper.mapToResponse(promotionRepository.save(promotion));
    }

    @Override
    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ErrorException("Promotion not found with id: " + id, ErrorCode.ERROR_MODEL_NOT_FOUND));
        promotionRepository.delete(promotion);
    }

    @Override
    public PromotionResponse getPromotionByCode(String code) {
        Promotion promotion = promotionRepository.findPromotionByCode(code)
                .orElseThrow(() -> new ErrorException("Promotion not found with code: " + code, ErrorCode.ERROR_MODEL_NOT_FOUND));
        return promotionMapper.mapToResponse(promotion);
    }

    @Override
    public Page<PromotionResponse> getAllPromotions(Pageable pageable) {
        Page<Promotion> promotions = promotionRepository.findAll(pageable);
        if (promotions.getTotalElements() == 0) {
            throw  new ErrorException("No promotions found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return promotions.map(promotionMapper::mapToResponse);
    }

    @Override
    public boolean activatePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ErrorException("Promotion not found with id: " + id, ErrorCode.ERROR_MODEL_NOT_FOUND));
        promotion.setIsActive(true);
        promotionRepository.save(promotion);
        return true;
    }

    @Override
    public boolean deactivatePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ErrorException("Promotion not found with id: " + id, ErrorCode.ERROR_MODEL_NOT_FOUND));
        promotion.setIsActive(false);
        promotionRepository.save(promotion);
        return true;
    }

    @Override
    public Page<PromotionResponse> getActivePromotions(Pageable pageable) {
        Page<Promotion> promotion = promotionRepository.findPromotionsByIsActive(true, pageable);
        if (promotion.getTotalElements() == 0) {
            throw new ErrorException("No active promotions found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return promotion.map(promotionMapper::mapToResponse);
    }
}
