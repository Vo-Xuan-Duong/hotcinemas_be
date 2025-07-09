package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.responses.MovieResponse;
import com.example.hotcinemas_be.models.Movie;
import org.springframework.stereotype.Service;

@Service
public class MovieMapper {
    private final GenreMapper genreMapper;

    public MovieMapper(GenreMapper genreMapper) {
        this.genreMapper = genreMapper;
    }

    public MovieResponse mapToResponse(Movie movie) {
        if (movie == null) {
            return null;
        }

        return MovieResponse.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .director(movie.getDirector())
                .genreResponse(movie.getGenres() != null ? movie.getGenres().stream().map(genreMapper::mapToResponse).toList() : null)
                .synopsis(movie.getSynopsis())
                .posterUrl(movie.getPosterUrl())
                .backdropUrl(movie.getBackdropUrl())
                .trailerUrl(movie.getTrailerUrl())
                .releaseDate(movie.getReleaseDate() != null ? movie.getReleaseDate() : null)
                .durationMinutes(movie.getDurationMinutes())
                .language(movie.getLanguage())
                .country(movie.getCountry())
                .casts(movie.getCasts())
                .audioOptions(movie.getAudioOptions())
                .ageLabel(movie.getAgeLabel())
                .type(movie.getType())
                .format(movie.getFormat())
                .rating(movie.getRating())
                .isActive(movie.getIsActive())
                .build();
    }
}
