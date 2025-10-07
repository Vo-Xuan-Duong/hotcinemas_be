package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.cinema_cluster.request.CinemaClusterRequest;
import com.example.hotcinemas_be.dtos.cinema_cluster.response.CinemaClusterResponse;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.CinemaClusterMapper;
import com.example.hotcinemas_be.models.Cinema;
import com.example.hotcinemas_be.models.CinemaCluster;
import com.example.hotcinemas_be.repositorys.CinemaClusterRepository;
import com.example.hotcinemas_be.repositorys.CinemaRepository;
import com.example.hotcinemas_be.services.CinemaClusterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaClusterServiceImpl implements CinemaClusterService {

    private final CinemaClusterRepository cinemaClusterRepository;
    private final CinemaRepository cinemaRepository;
    private final CinemaClusterMapper cinemaClusterMapper;

    @Override
    public CinemaClusterResponse createCinemaCluster(CinemaClusterRequest cinemaClusterRequest) {
        CinemaCluster cinemaCluster = CinemaCluster.builder()
                .name(cinemaClusterRequest.getName())
                .description(cinemaClusterRequest.getDescription())
                .iconURL(cinemaClusterRequest.getIconURL())
                .build();
        return cinemaClusterMapper.mapToResponse(cinemaClusterRepository.save(cinemaCluster));
    }

    @Override
    public CinemaClusterResponse getCinemaClusterById(Long id) {
        CinemaCluster cinemaCluster = cinemaClusterRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.ERROR_CINEMA_CLUSTER_NOT_FOUND));
        return cinemaClusterMapper.mapToResponse(cinemaCluster);
    }

    @Override
    public CinemaClusterResponse updateCinemaCluster(Long id, CinemaClusterRequest cinemaClusterRequest) {
        CinemaCluster cinemaCluster = cinemaClusterRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.ERROR_CINEMA_CLUSTER_NOT_FOUND));
        cinemaCluster.setName(cinemaClusterRequest.getName());
        cinemaCluster.setDescription(cinemaClusterRequest.getDescription());
        cinemaCluster.setIconURL(cinemaClusterRequest.getIconURL());
        return cinemaClusterMapper.mapToResponse(cinemaClusterRepository.save(cinemaCluster));
    }

    @Override
    public void deleteCinemaCluster(Long id) {
        CinemaCluster cinemaCluster = cinemaClusterRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.ERROR_CINEMA_CLUSTER_NOT_FOUND));
        cinemaClusterRepository.delete(cinemaCluster);
    }

    @Override
    public Page<CinemaClusterResponse> getPageCinemaClusters(Pageable pageable) {
        Page<CinemaCluster> cinemaClusters = cinemaClusterRepository.findAll(pageable);
        if(cinemaClusters.isEmpty()){
            throw new ErrorException(ErrorCode.ERROR_CINEMA_CLUSTER_NOT_FOUND);
        }
        return cinemaClusters.map(cinemaClusterMapper::mapToResponse);
    }

    @Override
    public List<CinemaClusterResponse> getAllCinemaClusters() {
        List<CinemaCluster> cinemaClusters = cinemaClusterRepository.findAll();
        if(cinemaClusters.isEmpty()){
            throw new ErrorException(ErrorCode.ERROR_CINEMA_CLUSTER_NOT_FOUND);
        }
        return cinemaClusters.stream().map(cinemaClusterMapper::mapToResponse).toList();
    }

    @Override
    public CinemaClusterResponse addCinemaToCluster(Long clusterId, Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(() -> new ErrorException(ErrorCode.ERROR_CINEMA_NOT_FOUND));
        CinemaCluster cinemaCluster = cinemaClusterRepository.findById(clusterId).orElseThrow(() -> new ErrorException(ErrorCode.ERROR_CINEMA_CLUSTER_NOT_FOUND));
        cinemaCluster.getCinemas().add(cinema);
        cinema.setCinemaCluster(cinemaCluster);
        cinemaRepository.save(cinema);
        return cinemaClusterMapper.mapToResponse(cinemaClusterRepository.save(cinemaCluster));
    }

    @Override
    public CinemaClusterResponse removeCinemaFromCluster(Long clusterId, Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(() -> new ErrorException(ErrorCode.ERROR_CINEMA_NOT_FOUND));
        CinemaCluster cinemaCluster = cinemaClusterRepository.findById(clusterId).orElseThrow(() -> new ErrorException(ErrorCode.ERROR_CINEMA_CLUSTER_NOT_FOUND));
        cinemaCluster.getCinemas().remove(cinema);
        cinema.setCinemaCluster(null);
        cinemaRepository.save(cinema);
        return cinemaClusterMapper.mapToResponse(cinemaClusterRepository.save(cinemaCluster));
    }
}
