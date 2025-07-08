package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre,Long> {
}
