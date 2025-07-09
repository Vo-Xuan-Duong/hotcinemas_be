package com.example.hotcinemas_be.controllers;

import com.example.hotcinemas_be.dtos.ResponseData;
import com.example.hotcinemas_be.dtos.requests.MovieRequest;
import com.example.hotcinemas_be.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/movies")
@Tag(name = "Movie Management", description = "APIs for managing movies in the cinema system")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(summary = "Get all movies", description = "Retrieve a list of all movies in the cinema system")
    @GetMapping
    public ResponseEntity<?> getAllMovies(Pageable pageable) {
        try{
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully retrieved all movies in the cinema system")
                    .data(movieService.getAllMovies(pageable))
                    .build();
            return ResponseEntity.status(200).body(responseData);
        }catch(Exception ex){
            return ResponseEntity.status(500).body("An error occurred while fetching movies: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get movie by ID", description = "Retrieve a specific movie by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully retrieved movie with ID: " + id)
                    .data(movieService.getMovieById(id))
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while fetching the movie: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get movies by genre", description = "Retrieve all movies associated with a specific genre")
    @GetMapping("/genre/{genre}")
    public ResponseEntity<?> getMoviesByGenre(@PathVariable String genre, Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully retrieved movies for genre: " + genre)
                    .data(movieService.getMoviesByGenre(genre, pageable))
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while fetching movies by genre: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get movies coming soon", description = "Retrieve a list of movies that are coming soon to the cinema")
    @GetMapping("/coming-soon")
    public ResponseEntity<?> getComingSoonMovies(Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully retrieved coming soon movies")
                    .data(movieService.getComingSoonMovies(pageable))
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while fetching coming soon movies: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get movies now showing", description = "Retrieve a list of movies that are currently showing in the cinema")
    @GetMapping("/now-showing")
    public ResponseEntity<?> getNowShowingMovies(Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully retrieved now showing movies")
                    .data(movieService.getNowShowingMovies(pageable))
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while fetching now showing movies: " + ex.getMessage());
        }
    }

    @Operation(summary = "Create a new movie", description = "Allows an admin to create a new movie in the cinema system")
    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody MovieRequest movieRequest) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(201)
                    .message("Movie has been successfully created")
                    .data(movieService.createMovie(movieRequest))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(201).body(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error creating movie: " + ex.getMessage());
        }
    }
}
