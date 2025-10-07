package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    Optional<Cinema> findByName(String name);

    Page<Cinema> findByCity(String city, Pageable pageable);

    Page<Cinema> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT c FROM Cinema c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.city) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Cinema> searchCinemas(@Param("keyword") String keyword, Pageable pageable);

    List<Cinema> findByCinemaClusterId(Long cinemaClusterId);

    @Query("SELECT c FROM Cinema c WHERE c.cinemaCluster.id = :clusterId AND c.isActive = true")
    List<Cinema> findActiveCinemasByClusterId(@Param("clusterId") Long clusterId);
}
