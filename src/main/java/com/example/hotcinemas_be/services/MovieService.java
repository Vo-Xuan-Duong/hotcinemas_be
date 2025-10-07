package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.movie.requests.MovieRequest;
import com.example.hotcinemas_be.dtos.movie.responses.MovieResponse;
import com.example.hotcinemas_be.dtos.movie.responses.MovieListItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {
    public MovieResponse createMovie(MovieRequest movieRequest);

    public MovieResponse updateMovie(Long movieId, MovieRequest movieRequest);

    public MovieResponse getMovieById(Long movieId);

    public void deleteMovie(Long movieId);

    public Page<MovieListItemResponse> getAllMovies(Pageable pageable);

    Page<MovieListItemResponse> getMoviesByGenre(String genre, Pageable pageable);

    Page<MovieListItemResponse> getComingSoonMovies(Pageable pageable);

    Page<MovieListItemResponse> getNowShowingMovies(Pageable pageable);

    Page<MovieListItemResponse> searchMovies(String keyword, String genre, String language, Pageable pageable);

    public void deleteAllMovies();

    Page<MovieListItemResponse> getTopRatedMovies(Pageable pageable);
}
