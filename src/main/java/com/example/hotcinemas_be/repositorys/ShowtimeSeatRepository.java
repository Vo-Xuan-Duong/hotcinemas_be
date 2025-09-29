package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Showtime;
import com.example.hotcinemas_be.models.ShowtimeSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, Long> {
    List<ShowtimeSeat> findByShowtime(Showtime showtime);

    List<ShowtimeSeat> findAllByShowtime_ShowtimeId(Long showtimeShowtimeId);
}
