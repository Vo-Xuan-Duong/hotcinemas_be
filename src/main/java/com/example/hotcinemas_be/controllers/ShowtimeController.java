package com.example.hotcinemas_be.controllers;

import com.example.hotcinemas_be.dtos.ResponseData;
import com.example.hotcinemas_be.dtos.requests.ShowtimeRequest;
import com.example.hotcinemas_be.services.ShowtimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/showtimes")
@Tag(name = "Showtime Management", description = "APIs for managing showtimes")
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @Operation(summary = "Create a new showtime", description = "Creates a new showtime for a movie in a specific cinema hall.")
    @PostMapping("/room/{roomId}")
    public ResponseEntity<?> createShowtime(@PathVariable Long roomId,@RequestBody ShowtimeRequest showtimeRequest) {
        try{
            ResponseData<?> responseData = ResponseData.builder()
                    .status(201)
                    .message("Showtime created successfully")
                    .data(showtimeService.createShowtime(roomId, showtimeRequest))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(201).body(responseData);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body("Error creating showtime: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get showtime by ID", description = "Retrieves a showtime by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getShowtimeById(@PathVariable Long id) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtime retrieved successfully")
                    .data(showtimeService.getShowtimeById(id))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error retrieving showtime: " + ex.getMessage());
        }
    }

    @Operation(summary = "Update showtime by ID", description = "Updates an existing showtime by its ID.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateShowtime(@PathVariable Long id, @RequestBody ShowtimeRequest showtimeRequest) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtime updated successfully")
                    .data(showtimeService.updateShowtime(id, showtimeRequest))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error updating showtime: " + ex.getMessage());
        }
    }

    @Operation(summary = "Delete showtime by ID", description = "Deletes a showtime by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShowtime(@PathVariable Long id) {
        try {
            showtimeService.deleteShowtime(id);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtime deleted successfully")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error deleting showtime: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get all showtimes", description = "Retrieves all showtimes with pagination.")
    @GetMapping
    public ResponseEntity<?> getAllShowtimes(Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtimes retrieved successfully")
                    .data(showtimeService.getAllShowTimes(pageable))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error retrieving showtimes: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get showtimes by movie ID", description = "Retrieves showtimes for a specific movie with pagination.")
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getShowtimesByMovieId(@PathVariable Long movieId, Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtimes for movie retrieved successfully")
                    .data(showtimeService.getShowtimesByMovieId(movieId, pageable))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error retrieving showtimes for movie: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get showtimes by room ID", description = "Retrieves showtimes for a specific cinema hall with pagination.")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getShowtimesByRoomId(@PathVariable Long roomId, Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtimes for room retrieved successfully")
                    .data(showtimeService.getShowtimesByRoomId(roomId, pageable))
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error retrieving showtimes for room: " + ex.getMessage());
        }
    }

    @Operation(summary = "Activate showtime", description = "Activates a showtime by its ID.")
    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activateShowtime(@PathVariable Long id) {
        try {
            showtimeService.activateShowtime(id);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtime activated successfully")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error activating showtime: " + ex.getMessage());
        }
    }

    @Operation(summary = "Deactivate showtime", description = "Deactivates a showtime by its ID.")
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateShowtime(@PathVariable Long id) {
        try {
            showtimeService.deactivateShowtime(id);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtime deactivated successfully")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error deactivating showtime: " + ex.getMessage());
        }
    }

    @Operation(summary = "Delete showtimes by movie ID", description = "Deletes all showtimes for a specific movie.")
    @DeleteMapping("/movie/{movieId}")
    public ResponseEntity<?> deleteShowtimesByMovieId(@PathVariable Long movieId) {
        try {
            showtimeService.deleteShowtimesByMovieId(movieId);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtimes for movie deleted successfully")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error deleting showtimes for movie: " + ex.getMessage());
        }
    }

    @Operation(summary = "Delete showtimes by room ID", description = "Deletes all showtimes for a specific cinema hall.")
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<?> deleteShowtimesByRoomId(@PathVariable Long roomId) {
        try {
            showtimeService.deleteShowtimesByRoomId(roomId);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtimes for room deleted successfully")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error deleting showtimes for room: " + ex.getMessage());
        }
    }
}
