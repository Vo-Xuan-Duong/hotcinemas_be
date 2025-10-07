package com.example.hotcinemas_be.controllers;

import com.example.hotcinemas_be.dtos.common.ResponseData;
import com.example.hotcinemas_be.dtos.seat.requests.SeatRequest;
import com.example.hotcinemas_be.dtos.seat.responses.SeatResponse;
import com.example.hotcinemas_be.enums.SeatType;
import com.example.hotcinemas_be.services.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Seat Management", description = "APIs for managing seats")
public class SeatController {

    private final SeatService seatService;

    @Operation(summary = "Create a new seat", description = "This endpoint allows creating a new seat in a room.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Seat created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "409", description = "Seat already exists at this position")
    })
    @PostMapping
    public ResponseEntity<ResponseData<SeatResponse>> createSeat(
            @Valid @RequestBody SeatRequest seatRequest) {
        try {
            log.info("Creating new seat at position {}{} in room {}",
                    seatRequest.getRowLabel(), seatRequest.getSeatNumber(), seatRequest.getRoomId());
            SeatResponse seatResponse = seatService.createSeat(seatRequest);

            ResponseData<SeatResponse> responseData = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Seat has been successfully created")
                    .data(seatResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
        } catch (Exception ex) {
            log.error("Error creating seat: {}", ex.getMessage());
            ResponseData<SeatResponse> errorResponse = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Get a seat by ID", description = "This endpoint retrieves a seat by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat found"),
            @ApiResponse(responseCode = "404", description = "Seat not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<SeatResponse>> getSeatById(
            @Parameter(description = "Seat ID") @PathVariable Long id) {
        try {
            log.info("Retrieving seat with ID: {}", id);
            SeatResponse seat = seatService.getSeatById(id);

            ResponseData<SeatResponse> responseData = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seat retrieved successfully")
                    .data(seat)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving seat with ID {}: {}", id, ex.getMessage());
            ResponseData<SeatResponse> errorResponse = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Get seats by room ID", description = "This endpoint retrieves all seats for a specific room.")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ResponseData<List<SeatResponse>>> getSeatsByRoomId(
            @Parameter(description = "Room ID") @PathVariable Long roomId) {
        try {
            log.info("Retrieving seats for room ID: {}", roomId);
            List<SeatResponse> seats = seatService.getSeatsByRoomId(roomId);

            ResponseData<List<SeatResponse>> responseData = ResponseData.<List<SeatResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seats for room retrieved successfully")
                    .data(seats)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving seats for room {}: {}", roomId, ex.getMessage());
            ResponseData<List<SeatResponse>> errorResponse = ResponseData.<List<SeatResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving seats: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get active seats by room ID", description = "This endpoint retrieves all active seats for a specific room.")
    @GetMapping("/room/{roomId}/active")
    public ResponseEntity<ResponseData<List<SeatResponse>>> getActiveSeatsByRoomId(
            @Parameter(description = "Room ID") @PathVariable Long roomId) {
        try {
            log.info("Retrieving active seats for room ID: {}", roomId);
            List<SeatResponse> seats = seatService.getSeatsByRoomIdAndActive(roomId);

            ResponseData<List<SeatResponse>> responseData = ResponseData.<List<SeatResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Active seats for room retrieved successfully")
                    .data(seats)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving active seats for room {}: {}", roomId, ex.getMessage());
            ResponseData<List<SeatResponse>> errorResponse = ResponseData.<List<SeatResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving seats: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get seats by seat type", description = "This endpoint retrieves all seats with a specific seat type.")
    @GetMapping("/type/{seatType}")
    public ResponseEntity<ResponseData<List<SeatResponse>>> getSeatsBySeatType(
            @Parameter(description = "Seat type") @PathVariable SeatType seatType) {
        try {
            log.info("Retrieving seats with type: {}", seatType);
            List<SeatResponse> seats = seatService.getSeatsBySeatType(seatType);

            ResponseData<List<SeatResponse>> responseData = ResponseData.<List<SeatResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seats with type retrieved successfully")
                    .data(seats)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving seats with type {}: {}", seatType, ex.getMessage());
            ResponseData<List<SeatResponse>> errorResponse = ResponseData.<List<SeatResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving seats: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get seats by room ID and seat type", description = "This endpoint retrieves seats for a specific room and seat type.")
    @GetMapping("/room/{roomId}/type/{seatType}")
    public ResponseEntity<ResponseData<List<SeatResponse>>> getSeatsByRoomIdAndSeatType(
            @Parameter(description = "Room ID") @PathVariable Long roomId,
            @Parameter(description = "Seat type") @PathVariable SeatType seatType) {
        try {
            log.info("Retrieving seats for room {} with type {}", roomId, seatType);
            List<SeatResponse> seats = seatService.getSeatsByRoomIdAndSeatType(roomId, seatType);

            ResponseData<List<SeatResponse>> responseData = ResponseData.<List<SeatResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seats for room and type retrieved successfully")
                    .data(seats)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving seats for room {} with type {}: {}", roomId, seatType, ex.getMessage());
            ResponseData<List<SeatResponse>> errorResponse = ResponseData.<List<SeatResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving seats: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get seats by cinema ID", description = "This endpoint retrieves all seats for a specific cinema.")
    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<ResponseData<List<SeatResponse>>> getSeatsByCinemaId(
            @Parameter(description = "Cinema ID") @PathVariable Long cinemaId) {
        try {
            log.info("Retrieving seats for cinema ID: {}", cinemaId);
            List<SeatResponse> seats = seatService.getSeatsByCinemaId(cinemaId);

            ResponseData<List<SeatResponse>> responseData = ResponseData.<List<SeatResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seats for cinema retrieved successfully")
                    .data(seats)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving seats for cinema {}: {}", cinemaId, ex.getMessage());
            ResponseData<List<SeatResponse>> errorResponse = ResponseData.<List<SeatResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving seats: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get seat by room and position", description = "This endpoint retrieves a seat by room ID and position.")
    @GetMapping("/room/{roomId}/position/{rowLabel}/{seatNumber}")
    public ResponseEntity<ResponseData<SeatResponse>> getSeatByRoomAndPosition(
            @Parameter(description = "Room ID") @PathVariable Long roomId,
            @Parameter(description = "Row label") @PathVariable String rowLabel,
            @Parameter(description = "Seat number") @PathVariable String seatNumber) {
        try {
            log.info("Retrieving seat at position {}{} in room {}", rowLabel, seatNumber, roomId);
            SeatResponse seat = seatService.getSeatByRoomAndPosition(roomId, rowLabel, seatNumber);

            ResponseData<SeatResponse> responseData = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seat retrieved successfully")
                    .data(seat)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving seat at position {}{} in room {}: {}", rowLabel, seatNumber, roomId,
                    ex.getMessage());
            ResponseData<SeatResponse> errorResponse = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Update a seat", description = "This endpoint allows updating an existing seat.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat updated successfully"),
            @ApiResponse(responseCode = "404", description = "Seat not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<SeatResponse>> updateSeat(
            @Parameter(description = "Seat ID") @PathVariable Long id,
            @Valid @RequestBody SeatRequest seatRequest) {
        try {
            log.info("Updating seat with ID: {}", id);
            SeatResponse seatResponse = seatService.updateSeat(id, seatRequest);

            ResponseData<SeatResponse> responseData = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seat has been successfully updated")
                    .data(seatResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error updating seat with ID {}: {}", id, ex.getMessage());
            ResponseData<SeatResponse> errorResponse = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Partially update a seat", description = "This endpoint allows partially updating an existing seat.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat partially updated successfully"),
            @ApiResponse(responseCode = "404", description = "Seat not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData<SeatResponse>> partialUpdateSeat(
            @Parameter(description = "Seat ID") @PathVariable Long id,
            @RequestBody SeatRequest seatRequest) {
        try {
            log.info("Partially updating seat with ID: {}", id);
            SeatResponse seatResponse = seatService.updateSeat(id, seatRequest);

            ResponseData<SeatResponse> responseData = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seat has been partially updated")
                    .data(seatResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error partially updating seat with ID {}: {}", id, ex.getMessage());
            ResponseData<SeatResponse> errorResponse = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Activate a seat", description = "This endpoint activates a seat.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat activated successfully"),
            @ApiResponse(responseCode = "404", description = "Seat not found")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ResponseData<SeatResponse>> activateSeat(
            @Parameter(description = "Seat ID") @PathVariable Long id) {
        try {
            log.info("Activating seat with ID: {}", id);
            SeatResponse seatResponse = seatService.activateSeat(id);

            ResponseData<SeatResponse> responseData = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seat has been successfully activated")
                    .data(seatResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error activating seat with ID {}: {}", id, ex.getMessage());
            ResponseData<SeatResponse> errorResponse = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Deactivate a seat", description = "This endpoint deactivates a seat.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Seat not found")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ResponseData<SeatResponse>> deactivateSeat(
            @Parameter(description = "Seat ID") @PathVariable Long id) {
        try {
            log.info("Deactivating seat with ID: {}", id);
            SeatResponse seatResponse = seatService.deactivateSeat(id);

            ResponseData<SeatResponse> responseData = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seat has been successfully deactivated")
                    .data(seatResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error deactivating seat with ID {}: {}", id, ex.getMessage());
            ResponseData<SeatResponse> errorResponse = ResponseData.<SeatResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Delete a seat", description = "This endpoint allows deleting a seat by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Seat not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteSeat(
            @Parameter(description = "Seat ID") @PathVariable Long id) {
        try {
            log.info("Deleting seat with ID: {}", id);
            seatService.deleteSeat(id);

            ResponseData<Void> responseData = ResponseData.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("Seat has been successfully deleted")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error deleting seat with ID {}: {}", id, ex.getMessage());
            ResponseData<Void> errorResponse = ResponseData.<Void>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Create seats for room", description = "This endpoint creates multiple seats for a room.")
    @PostMapping("/room/{roomId}/create-bulk")
    public ResponseEntity<ResponseData<Void>> createSeatsForRoom(
            @Parameter(description = "Room ID") @PathVariable Long roomId,
            @RequestParam Integer rowsCount,
            @RequestParam Integer seatsPerRow) {
        try {
            log.info("Creating {} rows x {} seats for room {}", rowsCount, seatsPerRow, roomId);
            seatService.createSeatsForRoom(roomId, rowsCount, seatsPerRow);

            ResponseData<Void> responseData = ResponseData.<Void>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Seats have been successfully created for room")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
        } catch (Exception ex) {
            log.error("Error creating seats for room {}: {}", roomId, ex.getMessage());
            ResponseData<Void> errorResponse = ResponseData.<Void>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Delete all seats by room ID", description = "This endpoint deletes all seats for a specific room.")
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<ResponseData<Void>> deleteSeatsByRoomId(
            @Parameter(description = "Room ID") @PathVariable Long roomId) {
        try {
            log.info("Deleting all seats for room ID: {}", roomId);
            seatService.deleteSeatsByRoomId(roomId);

            ResponseData<Void> responseData = ResponseData.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("All seats for room have been successfully deleted")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error deleting seats for room {}: {}", roomId, ex.getMessage());
            ResponseData<Void> errorResponse = ResponseData.<Void>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
