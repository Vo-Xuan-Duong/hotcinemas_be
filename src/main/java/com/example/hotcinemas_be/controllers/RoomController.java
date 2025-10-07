package com.example.hotcinemas_be.controllers;

import com.example.hotcinemas_be.dtos.common.ResponseData;
import com.example.hotcinemas_be.dtos.room.requests.RoomRequest;
import com.example.hotcinemas_be.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
@Tag(name = "Room Management", description = "APIs for managing rooms in the cinema system")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Get all rooms", description = "Retrieve a list of all rooms in the cinema system")
    @GetMapping
    public ResponseEntity<?> getAllRooms(Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully retrieved all rooms")
                    .data(roomService.getPageRooms(pageable))
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while fetching rooms: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get room by ID", description = "Retrieve a specific room by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(Long id) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully retrieved room with ID: " + id)
                    .data(roomService.getRoomById(id))
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while fetching the room: " + ex.getMessage());
        }
    }

    @Operation(summary = "Get rooms by cinema ID", description = "Retrieve all rooms associated with a specific cinema ID")
    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<?> getRoomsByCinemaId(Long cinemaId, Pageable pageable) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully retrieved rooms for cinema ID: " + cinemaId)
                    .data(roomService.getPageRoomsByCinemaId(cinemaId, pageable))
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body("An error occurred while fetching rooms for cinema ID: " + ex.getMessage());
        }
    }

    @Operation(summary = "Create a new room", description = "Create a new room in the cinema system")
    @PostMapping("/cinema/{cinemaId}")
    public ResponseEntity<?> createRoom(@PathVariable Long cinemaId, @RequestBody RoomRequest roomRequest) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(201)
                    .message("Successfully created a new room")
                    .data(roomService.createRoom(cinemaId, roomRequest))
                    .build();
            return ResponseEntity.status(201).body(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while creating the room: " + ex.getMessage());
        }
    }

    @Operation(summary = "Update a room", description = "Update an existing room in the cinema system")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        try {
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully updated room with ID: " + id)
                    .data(roomService.updateRoom(id, roomRequest))
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while updating the room: " + ex.getMessage());
        }
    }

    @Operation(summary = "Delete a room", description = "Delete a specific room by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully deleted room with ID: " + id)
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while deleting the room: " + ex.getMessage());
        }
    }

    @Operation(summary = "Delete all rooms by cinema ID", description = "Delete all rooms associated with a specific cinema ID")
    @DeleteMapping("/cinema/{cinemaId}")
    public ResponseEntity<?> deleteRoomsByCinemaId(@PathVariable Long cinemaId) {
        try {
            roomService.deleteRoomsByCinemaId(cinemaId);
            ResponseData<?> responseData = ResponseData.builder()
                    .status(200)
                    .message("Successfully deleted all rooms for cinema ID: " + cinemaId)
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body("An error occurred while deleting rooms for cinema ID: " + ex.getMessage());
        }
    }

}
