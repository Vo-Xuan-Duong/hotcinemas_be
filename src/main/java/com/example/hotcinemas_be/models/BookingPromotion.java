package com.example.hotcinemas_be.models;

// This is a join table. For simple join tables, JPA often handles it implicitly with @ManyToMany.
// However, if the join table has extra columns (like discount_applied_amount), you need an explicit entity.

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable; // Required for composite primary key
import java.math.BigDecimal;

@Entity
@Table(name = "booking_promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingPromotion implements Serializable {

    @EmbeddedId
    private BookingPromotionId id; // Composite primary key

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookingId") // Maps the bookingId from the EmbeddedId to this association
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("promotionId") // Maps the promotionId from the EmbeddedId to this association
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Column(name = "discount_applied_amount", nullable = false)
    private BigDecimal discountAppliedAmount; // DECIMAL(10,2) in DB, Double in Java

    // Composite Primary Key Class
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode // Important for composite key
    public static class BookingPromotionId implements Serializable {
        private Long bookingId;
        private Long promotionId;
    }
}