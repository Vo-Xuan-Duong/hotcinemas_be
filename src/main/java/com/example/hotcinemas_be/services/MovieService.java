package com.example.hotcinemas_be.services;


import com.example.hotcinemas_be.dtos.requests.MovieRequest;
import com.example.hotcinemas_be.dtos.responses.MovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {
    public MovieResponse createMovie(MovieRequest movieRequest);
    public MovieResponse updateMovie(Long movieId, MovieRequest movieRequest);
    public MovieResponse getMovieById(Long movieId);
    public void deleteMovie(Long movieId);
    public Page<MovieResponse> getAllMovies(Pageable pageable);
    Object getMoviesByGenre(String genre, Pageable pageable);

    Object getComingSoonMovies(Pageable pageable);

    Object getNowShowingMovies(Pageable pageable);
}
