package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.movie.responses.MovieResponse;
import com.example.hotcinemas_be.dtos.movie.responses.MovieListItemResponse;
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
                .id(movie.getId())
                .title(movie.getTitle())
                .originalTitle(movie.getOriginalTitle())
                .tagline(movie.getTagline())
                .overview(movie.getOverview())
                .durationMinutes(movie.getDurationMinutes())
                .releaseDate(movie.getReleaseDate())
                .originalLanguage(movie.getOriginalLanguage())
                .format(movie.getFormat())
                .ageRating(movie.getAgeRating())
                .trailerUrl(movie.getTrailerUrl())
                .posterPath(movie.getPosterPath())
                .backdropPath(movie.getBackdropPath())
                .genres(movie.getGenres() != null ? movie.getGenres().stream().map(genreMapper::mapToResponse).toList()
                        : null)
                .originCountry(movie.getOriginCountry())
                .casts(movie.getCasts())
                .voteAverage(movie.getVoteAverage())
                .voteCount(movie.getVoteCount())
                .isActive(movie.getIsActive())
                .build();
    }

    public MovieListItemResponse mapToListItem(Movie movie) {
        if (movie == null) {
            return null;
        }

        return MovieListItemResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .releaseDate(movie.getReleaseDate())
                .posterPath(movie.getPosterPath())
                .backdropPath(movie.getBackdropPath())
                .duration(movie.getDurationMinutes())
                .format(movie.getFormat())
                .genres(movie.getGenres() != null ? movie.getGenres().stream().map(genreMapper::mapToResponse).toList()
                        : null)
                .ageRating(movie.getAgeRating())
                .voteAverage(movie.getVoteAverage())
                .isActive(movie.getIsActive())
                .build();
    }
}
