package com.example.hotcinemas_be.dtos.requests;

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
public class MovieRequest {
    private String title;
    private String director;
    private List<Long> genreIds;
    private String synopsis;
    private String posterUrl;
    private String backdropUrl;
    private String trailerUrl;
    private LocalDateTime releaseDate;
    private Integer durationMinutes;
    private String language;
    private String country;
    private List<String> casts;
    private List<AudioOption> audioOptions;
    private String ageLabel;
    private String type;
    private String format;
}
