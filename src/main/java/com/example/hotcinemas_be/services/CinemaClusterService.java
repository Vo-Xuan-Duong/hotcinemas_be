package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.cinema_cluster.request.CinemaClusterRequest;
import com.example.hotcinemas_be.dtos.cinema_cluster.response.CinemaClusterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CinemaClusterService {
    CinemaClusterResponse createCinemaCluster(CinemaClusterRequest cinemaClusterRequest);
    CinemaClusterResponse getCinemaClusterById(Long id);
    CinemaClusterResponse updateCinemaCluster(Long id, CinemaClusterRequest cinemaClusterRequest);
    void deleteCinemaCluster(Long id);
    Page<CinemaClusterResponse> getPageCinemaClusters(Pageable pageable);
    List<CinemaClusterResponse> getAllCinemaClusters();
    CinemaClusterResponse addCinemaToCluster(Long clusterId, Long cinemaId);
    CinemaClusterResponse removeCinemaFromCluster(Long clusterId, Long cinemaId);

}
