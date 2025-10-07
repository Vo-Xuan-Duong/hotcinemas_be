package com.example.hotcinemas_be.dtos.cinema_cluster.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CinemaClusterRequest {
    private String name;
    private String description;
    private String iconURL;
}
