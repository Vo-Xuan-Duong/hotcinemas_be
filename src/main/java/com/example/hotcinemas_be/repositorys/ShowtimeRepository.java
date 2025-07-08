package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowtimeRepository extends JpaRepository<Showtime,Long> {
}
