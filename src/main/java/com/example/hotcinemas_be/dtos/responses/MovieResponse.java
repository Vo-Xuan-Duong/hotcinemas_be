package com.example.hotcinemas_be.dtos.responses;

import com.example.hotcinemas_be.enums.AudioOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {
    private Long movieId;
    private String title;
    private String director;
    private List<GenreResponse> genreResponse; // Comma-separated genre names
    private String synopsis;
    private String posterUrl;
    private String backdropUrl;
    private String trailerUrl;
    private LocalDateTime releaseDate; // ISO 8601 format
    private Integer durationMinutes;
    private String language;
    private String country;
    private List<String> casts; // Comma-separated cast names
    private List<AudioOption> audioOptions; // Comma-separated audio options
    private String ageLabel;// e.g., "PG-13", "R"
    private String type; // e.g., "Movie", "Series"
    private String format; // e.g., "2D", "3D", "IMAX"
    private Double rating;
    private Boolean isActive; // Indicates if the movie is currently active or not
}
