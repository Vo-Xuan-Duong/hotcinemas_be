package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Long> {
}
