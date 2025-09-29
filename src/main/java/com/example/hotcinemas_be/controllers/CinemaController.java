package com.example.hotcinemas_be.controllers;

import com.example.hotcinemas_be.dtos.ResponseData;
import com.example.hotcinemas_be.dtos.requests.CinemaRequest;
import com.example.hotcinemas_be.services.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/cinemas")
@Tag(name = "Cinemas", description = "API for managing cinemas")
public class CinemaController {
    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @Operation(summary = "Create a new cinema", description = "This endpoint allows an admin to create a new cinema.")
    @PostMapping
    public ResponseEntity<?> createCinema(@RequestBody CinemaRequest cinemaRequest) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(201)
                    .message("Cinema has been successfully created")
                    .data(cinemaService.createCinema(cinemaRequest))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Get all cinemas", description = "This endpoint retrieves all cinemas.")
    @GetMapping
    public ResponseEntity<?> getAllCinemas(Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Cinemas retrieved successfully")
                    .data(cinemaService.getAllCinemas(pageable))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Get a cinema by ID", description = "This endpoint retrieves a cinema by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCinemaById(@PathVariable Long id) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Cinema retrieved successfully")
                    .data(cinemaService.getCinemaById(id))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Update a cinema", description = "This endpoint allows an admin to update an existing cinema.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCinema(@PathVariable Long id, @RequestBody CinemaRequest cinemaRequest) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Cinema has been successfully updated")
                    .data(cinemaService.updateCinema(id, cinemaRequest))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Delete a cinema", description = "This endpoint allows an admin to delete a cinema by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCinema(@PathVariable Long id) {
        try {
            cinemaService.deleteCinema(id);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Cinema has been successfully deleted")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Get all cinemas by city", description = "This endpoint retrieves all cinemas in a specific city.")
    @GetMapping("/city/{city}")
    public ResponseEntity<?> getCinemasByCity(@PathVariable String city, Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Cinemas in " + city + " retrieved successfully")
                    .data(cinemaService.getCinemasByCity(city, pageable))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


}
