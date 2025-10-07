package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.genre.requests.GenreRequest;
import com.example.hotcinemas_be.dtos.genre.responses.GenreResponse;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.GenreMapper;
import com.example.hotcinemas_be.models.Genre;
import com.example.hotcinemas_be.repositorys.GenreRepository;
import com.example.hotcinemas_be.services.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    public GenreResponse createGenre(GenreRequest genreRequest) {
        Genre genre = Genre.builder()
                .id(genreRequest.getId())
                .name(genreRequest.getName())
                .build();
        return genreMapper.mapToResponse(genreRepository.save(genre));
    }

    @Override
    public GenreResponse updateGenre(Long genreId, GenreRequest genreRequest) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ErrorException("Genre not found with id: " + genreId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        genre.setId(genreRequest.getId());
        genre.setName(genreRequest.getName());
        return genreMapper.mapToResponse(genreRepository.save(genre));
    }

    @Override
    public void deleteGenre(Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ErrorException("Genre not found with id: " + genreId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        genreRepository.delete(genre);
    }

    @Override
    public GenreResponse getGenreById(Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ErrorException("Genre not found with id: " + genreId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        return genreMapper.mapToResponse(genre);
    }

    @Override
    public GenreResponse getGenreByName(String genreName) {
        Genre genre = genreRepository.findGenreByName(genreName)
                .orElseThrow(() -> new ErrorException("Genre not found with name: " + genreName,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        return genreMapper.mapToResponse(genre);
    }

    @Override
    public List<GenreResponse> getAllGenre() {
        List<Genre> genres = genreRepository.findAll();
        if (genres.isEmpty()) {
            throw new ErrorException("No genres found", ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return genres.stream().map(genreMapper::mapToResponse).collect(Collectors.toList());
    }
}
