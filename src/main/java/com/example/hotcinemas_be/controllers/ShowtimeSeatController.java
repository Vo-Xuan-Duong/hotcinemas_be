package com.example.hotcinemas_be.controllers;


import com.example.hotcinemas_be.dtos.ResponseData;
import com.example.hotcinemas_be.enums.SeatStatus;
import com.example.hotcinemas_be.services.ShowtimeSeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/showtime-seats")
@Tag(name = "Showtime Seat Management", description = "APIs for managing showtime seats")
public class ShowtimeSeatController {

    private final ShowtimeSeatService showtimeSeatService;

    public ShowtimeSeatController (ShowtimeSeatService showtimeSeatService) {
        this.showtimeSeatService = showtimeSeatService;
    }

    @Operation(summary = "Create seats for a showtime", description = "Creates seats for a specific showtime based on the room's seat configuration.")
    @PostMapping("/create/{showtimeId}")
    public ResponseEntity<?> createShowtimeSeats(@PathVariable Long showtimeId) {
        try {
            showtimeSeatService.createShowtimeSeats(showtimeId);

            ResponseData<?> responseData = ResponseData.builder()
                    .status(201)
                    .message("Seats created successfully for showtime ID: " + showtimeId)
                    .build();

            return ResponseEntity.status(201).body(responseData);

        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error creating seats: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get seats by showtime ID", description = "Retrieves all seats for a specific showtime.")
    @GetMapping("/{showtimeId}")
    public ResponseEntity<?> getSeatsByShowtimeId(@PathVariable Long showtimeId) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Seats retrieved successfully for showtime ID: " + showtimeId)
                    .data(showtimeSeatService.getShowtimeSeatsByShowtimeId(showtimeId))
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error retrieving seats: " + ex.getMessage());
        }
    }

    @Operation(summary = "Update status of a showtime seat", description = "Updates the status of a specific showtime seat.")
    @PutMapping("/{showtimeSeatId}/status/{status}")
    public ResponseEntity<?> updateShowtimeSeatStatus(@PathVariable Long showtimeSeatId, @PathVariable String status) {
        try {

            SeatStatus seatStatus = SeatStatus.valueOf(status.toUpperCase());

            showtimeSeatService.updateShowtimeSeatStatus(showtimeSeatId, seatStatus);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Showtime seat status updated successfully for ID: " + showtimeSeatId)
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error updating seat status: " + ex.getMessage());
        }
    }


}
