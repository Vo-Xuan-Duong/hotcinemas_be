package com.example.hotcinemas_be.controllers;

import com.example.hotcinemas_be.dtos.common.ResponseData;
import com.example.hotcinemas_be.dtos.genre.requests.GenreRequest;
import com.example.hotcinemas_be.dtos.genre.responses.GenreResponse;
import com.example.hotcinemas_be.services.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Genre Management", description = "APIs for managing movie genres")
public class GenreController {

    private final GenreService genreService;

    @Operation(summary = "Create a new genre", description = "Create a new movie genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Genre created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Genre already exists")
    })
    @PostMapping
    public ResponseEntity<ResponseData<GenreResponse>> createGenre(@Valid @RequestBody GenreRequest genreRequest) {
        log.info("Creating new genre with name: {}", genreRequest.getName());
        GenreResponse genreResponse = genreService.createGenre(genreRequest);

        ResponseData<GenreResponse> responseData = ResponseData.<GenreResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Genre created successfully")
                .data(genreResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @Operation(summary = "Get all genres", description = "Retrieve all available movie genres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genres retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No genres found")
    })
    @GetMapping
    public ResponseEntity<ResponseData<List<GenreResponse>>> getAllGenres() {
        log.info("Retrieving all genres");
        List<GenreResponse> genres = genreService.getAllGenre();

        ResponseData<List<GenreResponse>> responseData = ResponseData.<List<GenreResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Genres retrieved successfully")
                .data(genres)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Get genre by ID", description = "Retrieve a specific genre by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<GenreResponse>> getGenreById(
            @Parameter(description = "Genre ID", required = true) @PathVariable Long id) {
        log.info("Retrieving genre with ID: {}", id);
        GenreResponse genreResponse = genreService.getGenreById(id);

        ResponseData<GenreResponse> responseData = ResponseData.<GenreResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Genre retrieved successfully")
                .data(genreResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Get genre by name", description = "Retrieve a specific genre by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseData<GenreResponse>> getGenreByName(
            @Parameter(description = "Genre name", required = true) @PathVariable String name) {
        log.info("Retrieving genre with name: {}", name);
        GenreResponse genreResponse = genreService.getGenreByName(name);

        ResponseData<GenreResponse> responseData = ResponseData.<GenreResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Genre retrieved successfully")
                .data(genreResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Update genre", description = "Update an existing genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<GenreResponse>> updateGenre(
            @Parameter(description = "Genre ID", required = true) @PathVariable Long id,
            @Valid @RequestBody GenreRequest genreRequest) {
        log.info("Updating genre with ID: {} to name: {}", id, genreRequest.getName());
        GenreResponse genreResponse = genreService.updateGenre(id, genreRequest);

        ResponseData<GenreResponse> responseData = ResponseData.<GenreResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Genre updated successfully")
                .data(genreResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(responseData);
    }

    @Operation(summary = "Delete genre", description = "Delete a genre by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteGenre(
            @Parameter(description = "Genre ID", required = true) @PathVariable Long id) {
        log.info("Deleting genre with ID: {}", id);
        genreService.deleteGenre(id);

        ResponseData<Void> responseData = ResponseData.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Genre deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(responseData);
    }
}
