package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.cinema_cluster.response.CinemaClusterResponse;
import com.example.hotcinemas_be.models.CinemaCluster;
import org.springframework.stereotype.Component;

@Component
public class CinemaClusterMapper {
    public CinemaClusterResponse mapToResponse(CinemaCluster cinemaCluster) {
        return CinemaClusterResponse.builder()
                .id(cinemaCluster.getId())
                .name(cinemaCluster.getName())
                .description(cinemaCluster.getDescription())
                .iconURL(cinemaCluster.getIconURL())
                .createdAt(cinemaCluster.getCreatedAt())
                .updatedAt(cinemaCluster.getUpdatedAt())
                .build();
    }
}
