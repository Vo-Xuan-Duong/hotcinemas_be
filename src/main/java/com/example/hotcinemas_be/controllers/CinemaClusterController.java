package com.example.hotcinemas_be.controllers;

import com.example.hotcinemas_be.dtos.cinema_cluster.request.CinemaClusterRequest;
import com.example.hotcinemas_be.dtos.cinema_cluster.response.CinemaClusterResponse;
import com.example.hotcinemas_be.dtos.common.ResponseData;
import com.example.hotcinemas_be.services.CinemaClusterService;
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
@RequestMapping("/api/v1/cinema-clusters")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cinema Cluster Management", description = "APIs for managing cinema clusters")
public class CinemaClusterController {

    private final CinemaClusterService cinemaClusterService;

    @Operation(summary = "Create a new cinema cluster", description = "This endpoint allows an admin to create a new cinema cluster.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cinema cluster created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Cinema cluster with this name already exists")
    })
    @PostMapping
    public ResponseEntity<ResponseData<CinemaClusterResponse>> createCinemaCluster(
            @Valid @RequestBody CinemaClusterRequest cinemaClusterRequest) {
        try {
            log.info("Creating new cinema cluster with name: {}", cinemaClusterRequest.getName());
            CinemaClusterResponse cinemaClusterResponse = cinemaClusterService
                    .createCinemaCluster(cinemaClusterRequest);

            ResponseData<CinemaClusterResponse> responseData = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Cinema cluster has been successfully created")
                    .data(cinemaClusterResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
        } catch (Exception ex) {
            log.error("Error creating cinema cluster: {}", ex.getMessage());
            ResponseData<CinemaClusterResponse> errorResponse = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Get all cinema clusters", description = "This endpoint retrieves all cinema clusters with pagination.")
    @GetMapping
    public ResponseEntity<ResponseData<Page<CinemaClusterResponse>>> getAllCinemaClusters(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        try {
            log.info("Retrieving all cinema clusters with pagination");
            Page<CinemaClusterResponse> cinemaClusters = cinemaClusterService.getPageCinemaClusters(pageable);

            ResponseData<Page<CinemaClusterResponse>> responseData = ResponseData.<Page<CinemaClusterResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Cinema clusters retrieved successfully")
                    .data(cinemaClusters)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving cinema clusters: {}", ex.getMessage());
            ResponseData<Page<CinemaClusterResponse>> errorResponse = ResponseData
                    .<Page<CinemaClusterResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving cinema clusters: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get all cinema clusters (no pagination)", description = "This endpoint retrieves all cinema clusters without pagination.")
    @GetMapping("/all")
    public ResponseEntity<ResponseData<List<CinemaClusterResponse>>> getAllCinemaClustersList() {
        try {
            log.info("Retrieving all cinema clusters without pagination");
            List<CinemaClusterResponse> cinemaClusters = cinemaClusterService.getAllCinemaClusters();

            ResponseData<List<CinemaClusterResponse>> responseData = ResponseData.<List<CinemaClusterResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Cinema clusters retrieved successfully")
                    .data(cinemaClusters)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving cinema clusters: {}", ex.getMessage());
            ResponseData<List<CinemaClusterResponse>> errorResponse = ResponseData
                    .<List<CinemaClusterResponse>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving cinema clusters: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Get a cinema cluster by ID", description = "This endpoint retrieves a cinema cluster by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema cluster found"),
            @ApiResponse(responseCode = "404", description = "Cinema cluster not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CinemaClusterResponse>> getCinemaClusterById(
            @Parameter(description = "Cinema cluster ID") @PathVariable Long id) {
        try {
            log.info("Retrieving cinema cluster with ID: {}", id);
            CinemaClusterResponse cinemaCluster = cinemaClusterService.getCinemaClusterById(id);

            ResponseData<CinemaClusterResponse> responseData = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Cinema cluster retrieved successfully")
                    .data(cinemaCluster)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error retrieving cinema cluster with ID {}: {}", id, ex.getMessage());
            ResponseData<CinemaClusterResponse> errorResponse = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Update a cinema cluster", description = "This endpoint allows an admin to update an existing cinema cluster.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema cluster updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cinema cluster not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<CinemaClusterResponse>> updateCinemaCluster(
            @Parameter(description = "Cinema cluster ID") @PathVariable Long id,
            @Valid @RequestBody CinemaClusterRequest cinemaClusterRequest) {
        try {
            log.info("Updating cinema cluster with ID: {}", id);
            CinemaClusterResponse cinemaClusterResponse = cinemaClusterService.updateCinemaCluster(id,
                    cinemaClusterRequest);

            ResponseData<CinemaClusterResponse> responseData = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Cinema cluster has been successfully updated")
                    .data(cinemaClusterResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error updating cinema cluster with ID {}: {}", id, ex.getMessage());
            ResponseData<CinemaClusterResponse> errorResponse = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Partially update a cinema cluster", description = "This endpoint allows an admin to partially update an existing cinema cluster.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema cluster partially updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cinema cluster not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData<CinemaClusterResponse>> partialUpdateCinemaCluster(
            @Parameter(description = "Cinema cluster ID") @PathVariable Long id,
            @RequestBody CinemaClusterRequest cinemaClusterRequest) {
        try {
            log.info("Partially updating cinema cluster with ID: {}", id);
            CinemaClusterResponse cinemaClusterResponse = cinemaClusterService.updateCinemaCluster(id,
                    cinemaClusterRequest);

            ResponseData<CinemaClusterResponse> responseData = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Cinema cluster has been partially updated")
                    .data(cinemaClusterResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error partially updating cinema cluster with ID {}: {}", id, ex.getMessage());
            ResponseData<CinemaClusterResponse> errorResponse = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "Delete a cinema cluster", description = "This endpoint allows an admin to delete a cinema cluster by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema cluster deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cinema cluster not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteCinemaCluster(
            @Parameter(description = "Cinema cluster ID") @PathVariable Long id) {
        try {
            log.info("Deleting cinema cluster with ID: {}", id);
            cinemaClusterService.deleteCinemaCluster(id);

            ResponseData<Void> responseData = ResponseData.<Void>builder()
                    .status(HttpStatus.OK.value())
                    .message("Cinema cluster has been successfully deleted")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error deleting cinema cluster with ID {}: {}", id, ex.getMessage());
            ResponseData<Void> errorResponse = ResponseData.<Void>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Add cinema to cluster", description = "This endpoint adds a cinema to a cinema cluster.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema added to cluster successfully"),
            @ApiResponse(responseCode = "404", description = "Cinema or cluster not found")
    })
    @PostMapping("/{clusterId}/cinemas/{cinemaId}")
    public ResponseEntity<ResponseData<CinemaClusterResponse>> addCinemaToCluster(
            @Parameter(description = "Cinema cluster ID") @PathVariable Long clusterId,
            @Parameter(description = "Cinema ID") @PathVariable Long cinemaId) {
        try {
            log.info("Adding cinema {} to cluster {}", cinemaId, clusterId);
            CinemaClusterResponse cinemaClusterResponse = cinemaClusterService.addCinemaToCluster(clusterId, cinemaId);

            ResponseData<CinemaClusterResponse> responseData = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Cinema has been successfully added to cluster")
                    .data(cinemaClusterResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error adding cinema {} to cluster {}: {}", cinemaId, clusterId, ex.getMessage());
            ResponseData<CinemaClusterResponse> errorResponse = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @Operation(summary = "Remove cinema from cluster", description = "This endpoint removes a cinema from a cinema cluster.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema removed from cluster successfully"),
            @ApiResponse(responseCode = "404", description = "Cinema or cluster not found")
    })
    @DeleteMapping("/{clusterId}/cinemas/{cinemaId}")
    public ResponseEntity<ResponseData<CinemaClusterResponse>> removeCinemaFromCluster(
            @Parameter(description = "Cinema cluster ID") @PathVariable Long clusterId,
            @Parameter(description = "Cinema ID") @PathVariable Long cinemaId) {
        try {
            log.info("Removing cinema {} from cluster {}", cinemaId, clusterId);
            CinemaClusterResponse cinemaClusterResponse = cinemaClusterService.removeCinemaFromCluster(clusterId,
                    cinemaId);

            ResponseData<CinemaClusterResponse> responseData = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Cinema has been successfully removed from cluster")
                    .data(cinemaClusterResponse)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            log.error("Error removing cinema {} from cluster {}: {}", cinemaId, clusterId, ex.getMessage());
            ResponseData<CinemaClusterResponse> errorResponse = ResponseData.<CinemaClusterResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
