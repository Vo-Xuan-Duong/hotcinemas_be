package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.requests.MovieRequest;
import com.example.hotcinemas_be.dtos.responses.MovieResponse;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.MovieMapper;
import com.example.hotcinemas_be.models.Genre;
import com.example.hotcinemas_be.models.Movie;
import com.example.hotcinemas_be.repositorys.GenreRepository;
import com.example.hotcinemas_be.repositorys.MovieRepository;
import com.example.hotcinemas_be.services.MovieService;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final GenreRepository genreRepository;

    public  MovieServiceImpl(MovieRepository movieRepository,
                             MovieMapper movieMapper,
                             GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.genreRepository = genreRepository;
    }

    @Override
    public MovieResponse createMovie(MovieRequest movieRequest) {
        Movie movie = new Movie();
        movie.setTitle(movieRequest.getTitle());
        movie.setDirector(movieRequest.getDirector());
        movie.setSynopsis(movieRequest.getSynopsis());
        movie.setPosterUrl(movieRequest.getPosterUrl());
        movie.setBackdropUrl(movieRequest.getBackdropUrl());
        movie.setTrailerUrl(movieRequest.getTrailerUrl());
        movie.setReleaseDate(movieRequest.getReleaseDate());
        movie.setDurationMinutes(movieRequest.getDurationMinutes());
        movie.setLanguage(movieRequest.getLanguage());
        movie.setCountry(movieRequest.getCountry());
        movie.setCasts(movieRequest.getCasts());
        movie.setAudioOptions(movieRequest.getAudioOptions());
        movie.setAgeLabel(movieRequest.getAgeLabel());
        movie.setType(movieRequest.getType());
        movie.setFormat(movieRequest.getFormat());
        movie.setIsActive(true); // Assuming new movies are active by default
        movie.setRating(0.0); // Default rating, can be updated later
        Set<Genre> genres = new HashSet<>();
        movieRequest.getGenreIds().forEach(genreId -> {
            genreRepository.findById(genreId).ifPresent(genres::add);
        });
        movie.setGenres(genres);
        return movieMapper.mapToResponse(movieRepository.save(movie));
    }

    @Override
    public MovieResponse updateMovie(Long movieId, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ErrorException("Movie not found with id: " + movieId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        movie.setTitle(movieRequest.getTitle());
        movie.setDirector(movieRequest.getDirector());
        movie.setSynopsis(movieRequest.getSynopsis());
        movie.setPosterUrl(movieRequest.getPosterUrl());
        movie.setBackdropUrl(movieRequest.getBackdropUrl());
        movie.setTrailerUrl(movieRequest.getTrailerUrl());
        movie.setReleaseDate(movieRequest.getReleaseDate());
        movie.setDurationMinutes(movieRequest.getDurationMinutes());
        movie.setLanguage(movieRequest.getLanguage());
        movie.setCountry(movieRequest.getCountry());
        movie.setCasts(movieRequest.getCasts());
        movie.setAudioOptions(movieRequest.getAudioOptions());
        movie.setAgeLabel(movieRequest.getAgeLabel());
        movie.setType(movieRequest.getType());
        movie.setFormat(movieRequest.getFormat());
        movie.setIsActive(true); // Assuming new movies are active by default
        movie.setRating(0.0); // Default rating, can be updated later
        Set<Genre> genres = new HashSet<>();
        movieRequest.getGenreIds().forEach(genreId -> {
            genreRepository.findById(genreId).ifPresent(genres::add);
        });
        movie.setGenres(genres);
        return movieMapper.mapToResponse(movieRepository.save(movie));
    }

    @Override
    public MovieResponse getMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ErrorException("Movie not found with id: " + movieId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        return movieMapper.mapToResponse(movie);
    }

    @Override
    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ErrorException("Movie not found with id: " + movieId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        movieRepository.delete(movie);
    }

    @Override
    public Page<MovieResponse> getAllMovies(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        if (moviePage.isEmpty()) {
            throw new ErrorException("No movies found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return moviePage.map(movieMapper::mapToResponse);
    }

    @Override
    public Object getMoviesByGenre(String genre, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findByGenres_Name(genre, pageable);
        if (moviePage.isEmpty()) {
            throw new ErrorException("No movies found for genre: " + genre, ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return moviePage.map(movieMapper::mapToResponse);
    }

    @Override
    public Object getComingSoonMovies(Pageable pageable) {
        LocalDateTime currentDate = LocalDateTime.now();
        Page<Movie> moviePage = movieRepository.findComingSoonMovies(currentDate, pageable);
        if (moviePage.isEmpty()) {
            throw new ErrorException("No coming soon movies found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return moviePage.map(movieMapper::mapToResponse);
    }

    @Override
    public Object getNowShowingMovies(Pageable pageable) {
        LocalDateTime currentDate = LocalDateTime.now();
        Page<Movie> moviePage = movieRepository.findNowShowingMovies(currentDate, pageable);
        if (moviePage.isEmpty()) {
            throw new ErrorException("No now showing movies found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return moviePage.map(movieMapper::mapToResponse);
    }
}
