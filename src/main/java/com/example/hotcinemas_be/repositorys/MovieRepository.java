package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.enums.MovieStatus;
import com.example.hotcinemas_be.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface MovieRepository extends JpaRepository<Movie, Long> {
        Page<Movie> findByGenres_Name(String genre, Pageable pageable);

        Page<Movie> findMovieByStatus(MovieStatus status, Pageable pageable);

        @Query("SELECT m FROM Movie m WHERE " +
                        "(:keyword IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(m.originalTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(m.overview) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
                        "(:genre IS NULL OR EXISTS (SELECT g FROM m.genres g WHERE LOWER(g.name) = LOWER(:genre))) AND "
                        +
                        "(:language IS NULL OR LOWER(m.originalLanguage) = LOWER(:language)) AND " +
                        "m.isActive = true " +
                        "ORDER BY m.releaseDate DESC")
        Page<Movie> searchMovies(@Param("keyword") String keyword,
                        @Param("genre") String genre,
                        @Param("language") String language,
                        Pageable pageable);

        @Query("SELECT m FROM Movie m WHERE m.isActive = true ORDER BY m.voteAverage DESC, m.voteCount DESC")
        Page<Movie> findTopRated(Pageable pageable);
}
