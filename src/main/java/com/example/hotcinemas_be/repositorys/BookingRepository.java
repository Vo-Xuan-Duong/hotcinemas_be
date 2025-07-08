package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
