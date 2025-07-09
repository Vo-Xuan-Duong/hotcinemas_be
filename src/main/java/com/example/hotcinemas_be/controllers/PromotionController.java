package com.example.hotcinemas_be.controllers;

import com.example.hotcinemas_be.dtos.ResponseData;
import com.example.hotcinemas_be.dtos.requests.PromotionRequest;
import com.example.hotcinemas_be.services.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/promotions")
@Tag(name = "Promotions", description = "API for managing promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Operation(summary = "Create a new promotion", description = "This endpoint allows an admin to create a new promotion.")
    @PostMapping
    public ResponseEntity<?> createPromotion(@RequestBody PromotionRequest promotionRequest) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(201)
                    .message("Promotion has been successfully created")
                    .data(promotionService.createPromotion(promotionRequest))
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get all promotions", description = "This endpoint retrieves all promotions.")
    @GetMapping
    public ResponseEntity<?> getAllPromotions(Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Promotions retrieved successfully")
                    .data(promotionService.getAllPromotions(pageable))
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get a promotion by ID", description = "This endpoint retrieves a promotion by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPromotionById(@PathVariable Long id) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Promotion retrieved successfully")
                    .data(promotionService.getPromotionById(id))
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update a promotion", description = "This endpoint allows an admin to update an existing promotion.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePromotion(@PathVariable Long id, @RequestBody PromotionRequest promotionRequest) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Promotion has been successfully updated")
                    .data(promotionService.updatePromotion(id, promotionRequest))
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete a promotion", description = "This endpoint allows an admin to delete a promotion by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePromotion(@PathVariable Long id) {
        try {
            promotionService.deletePromotion(id);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Promotion has been successfully deleted")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Activate a promotion", description = "This endpoint allows an admin to activate a promotion by its ID.")
    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activatePromotion(@PathVariable Long id) {
        try {
            promotionService.activatePromotion(id);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Promotion has been successfully activated")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Deactivate a promotion", description = "This endpoint allows an admin to deactivate a promotion by its ID.")
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivatePromotion(@PathVariable Long id) {
        try {
            promotionService.deactivatePromotion(id);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Promotion has been successfully deactivated")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get a promotion by code", description = "This endpoint retrieves a promotion by its code.")
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getPromotionByCode(@PathVariable String code) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Promotion retrieved successfully")
                    .data(promotionService.getPromotionByCode(code))
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get all active promotions", description = "This endpoint retrieves all active promotions.")
    @GetMapping("/active-promotions")
    public ResponseEntity<?> getAllActivePromotions(Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Active promotions retrieved successfully")
                    .data(promotionService.getActivePromotions(pageable))
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
