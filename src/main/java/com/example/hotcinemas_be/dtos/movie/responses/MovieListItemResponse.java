package com.example.hotcinemas_be.dtos.movie.responses;

import com.example.hotcinemas_be.dtos.genre.responses.GenreResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieListItemResponse {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private String posterPath;
    private String backdropPath;
    private Integer duration; // in minutes
    private List<GenreResponse> genres;
    private String format;
    private String ageRating;
    private BigDecimal voteAverage;
    private Boolean isActive;
}
