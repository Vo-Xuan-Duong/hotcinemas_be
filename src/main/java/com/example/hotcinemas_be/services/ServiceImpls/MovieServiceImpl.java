package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.movie.requests.MovieRequest;
import com.example.hotcinemas_be.dtos.movie.responses.MovieResponse;
import com.example.hotcinemas_be.dtos.movie.responses.MovieListItemResponse;
import com.example.hotcinemas_be.enums.MovieStatus;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.MovieMapper;
import com.example.hotcinemas_be.models.Genre;
import com.example.hotcinemas_be.models.Movie;
import com.example.hotcinemas_be.repositorys.GenreRepository;
import com.example.hotcinemas_be.repositorys.MovieRepository;
import com.example.hotcinemas_be.services.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final GenreRepository genreRepository;

    public MovieServiceImpl(MovieRepository movieRepository,
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
        movie.setOriginalTitle(movieRequest.getOriginalTitle());
        movie.setTagline(movieRequest.getTagline());
        movie.setOverview(movieRequest.getOverview());
        movie.setPosterPath(movieRequest.getPosterPath());
        movie.setBackdropPath(movieRequest.getBackdropPath());
        movie.setTrailerUrl(movieRequest.getTrailerUrl());
        movie.setReleaseDate(movieRequest.getReleaseDate());
        movie.setDurationMinutes(movieRequest.getDurationMinutes());
        movie.setVoteAverage(movieRequest.getVoteAverage());
        movie.setVoteCount(movieRequest.getVoteCount());
        movie.setOriginalLanguage(movieRequest.getOriginalLanguage());
        movie.setCasts(movieRequest.getCasts());
        movie.setAgeRating(movieRequest.getAgeRating());
        movie.setFormat(movieRequest.getFormat());
        movie.setOriginCountry(movieRequest.getOriginCountry());
        Set<Genre> genres = new HashSet<>();
        if (movieRequest.getGenreIds() != null) {
            movieRequest.getGenreIds().forEach(genreId -> {
                genreRepository.findById(genreId).ifPresent(genres::add);
            });
        }
        movie.setGenres(genres.stream().toList());
        movie.setStatus(movieRequest.getStatus());
        return movieMapper.mapToResponse(movieRepository.save(movie));
    }

    @Override
    public MovieResponse updateMovie(Long movieId, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ErrorException("Movie not found with id: " + movieId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        movie.setTitle(movieRequest.getTitle());
        movie.setOriginalTitle(movieRequest.getOriginalTitle());
        movie.setTagline(movieRequest.getTagline());
        movie.setOverview(movieRequest.getOverview());
        movie.setPosterPath(movieRequest.getPosterPath());
        movie.setBackdropPath(movieRequest.getBackdropPath());
        movie.setTrailerUrl(movieRequest.getTrailerUrl());
        movie.setReleaseDate(movieRequest.getReleaseDate());
        movie.setVoteAverage(movieRequest.getVoteAverage());
        movie.setVoteCount(movieRequest.getVoteCount());
        movie.setDurationMinutes(movieRequest.getDurationMinutes());
        movie.setOriginalLanguage(movieRequest.getOriginalLanguage());
        movie.setCasts(movieRequest.getCasts());
        movie.setAgeRating(movieRequest.getAgeRating());
        movie.setFormat(movieRequest.getFormat());
        movie.setOriginCountry(movieRequest.getOriginCountry());
        Set<Genre> genres = new HashSet<>();
        if (movieRequest.getGenreIds() != null) {
            movieRequest.getGenreIds().forEach(genreId -> {
                genreRepository.findById(genreId).ifPresent(genres::add);
            });
        }
        movie.setGenres(genres.stream().toList());
        return movieMapper.mapToResponse(movieRepository.save(movie));
    }

    @Override
    public MovieResponse getMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ErrorException("Movie not found with id: " + movieId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        return movieMapper.mapToResponse(movie);
    }

    @Override
    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ErrorException("Movie not found with id: " + movieId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        movieRepository.delete(movie);
    }

    @Override
    public Page<MovieListItemResponse> getAllMovies(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        if (moviePage.isEmpty()) {
            throw new ErrorException("No movies found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return moviePage.map(movieMapper::mapToListItem);
    }

    @Override
    public Page<MovieListItemResponse> getMoviesByGenre(String genre, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findByGenres_Name(genre, pageable);
        if (moviePage.isEmpty()) {
            throw new ErrorException("No movies found for genre: " + genre, ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return moviePage.map(movieMapper::mapToListItem);
    }

    @Override
    public Page<MovieListItemResponse> getComingSoonMovies(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findMovieByStatus(MovieStatus.COMING_SOON, pageable);
        if (moviePage.isEmpty()) {
            throw new ErrorException("No coming soon movies found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return moviePage.map(movieMapper::mapToListItem);
    }

    @Override
    public Page<MovieListItemResponse> getNowShowingMovies(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findMovieByStatus(MovieStatus.NOW_SHOWING, pageable);
        if (moviePage.isEmpty()) {
            throw new ErrorException("No now showing movies found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return moviePage.map(movieMapper::mapToListItem);
    }

    @Override
    public Page<MovieListItemResponse> searchMovies(String keyword, String genre, String language, Pageable pageable) {
        String searchKeyword = (keyword != null && keyword.trim().isEmpty()) ? null : keyword;
        String searchGenre = (genre != null && genre.trim().isEmpty()) ? null : genre;
        String searchLanguage = (language != null && language.trim().isEmpty()) ? null : language;

        Page<Movie> moviePage = movieRepository.searchMovies(searchKeyword, searchGenre, searchLanguage, pageable);

        if (moviePage.isEmpty()) {
            throw new ErrorException("No movies found matching the search criteria", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return moviePage.map(movieMapper::mapToListItem);
    }

    @Override
    public void deleteAllMovies() {
        if (movieRepository.count() == 0) {
            throw new ErrorException("No movies to delete", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        movieRepository.deleteAll();
    }

    @Override
    public Page<MovieListItemResponse> getTopRatedMovies(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findTopRated(pageable);
        if (moviePage.isEmpty()) {
            throw new ErrorException("No top rated movies found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return moviePage.map(movieMapper::mapToListItem);
    }
}
