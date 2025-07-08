package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
