package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    Page<Movie> findByGenres_Name(String genre, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate > :currentDate AND m.isActive = true ORDER BY m.releaseDate ASC")
    Page<Movie> findComingSoonMovies(@Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= :currentDate AND m.isActive = true ORDER BY m.releaseDate DESC")
    Page<Movie> findNowShowingMovies(@Param("currentDate") LocalDateTime currentDate, Pageable pageable);
}
