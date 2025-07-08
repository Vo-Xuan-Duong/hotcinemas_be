package com.example.hotcinemas_be.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotcinemas_be.models.BookingPromotion;
import com.example.hotcinemas_be.models.BookingPromotion.BookingPromotionId;

public interface BookingPromotionRepository extends JpaRepository<BookingPromotion,BookingPromotionId> {
}
