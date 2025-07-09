package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.responses.GenreResponse;
import com.example.hotcinemas_be.models.Genre;
import org.springframework.stereotype.Service;

@Service
public class GenreMapper {
    public GenreResponse mapToResponse(Genre genre) {
        if (genre == null) {
            return null;
        }
        return GenreResponse.builder()
                .genreId(genre.getGenreId())
                .name(genre.getName())
                .build();
    }
}
