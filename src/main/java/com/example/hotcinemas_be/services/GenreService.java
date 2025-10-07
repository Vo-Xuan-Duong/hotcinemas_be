package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.genre.requests.GenreRequest;
import com.example.hotcinemas_be.dtos.genre.responses.GenreResponse;

import java.util.List;

public interface GenreService {
    public GenreResponse createGenre(GenreRequest genreRequest);

    public GenreResponse updateGenre(Long genreId, GenreRequest genreRequest);

    public void deleteGenre(Long genreId);

    public GenreResponse getGenreById(Long genreId);

    public GenreResponse getGenreByName(String genreName);

    public List<GenreResponse> getAllGenre();

}
