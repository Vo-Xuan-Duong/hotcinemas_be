package com.example.hotcinemas_be.dtos.cinema_cluster.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CinemaClusterResponse {
    private Long id;
    private String name;
    private String description;
    private String iconURL;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
