package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Showtime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    Page<Showtime> findByMovie_Id(Long movieId, Pageable pageable);

    List<Showtime> findByMovie_Id(Long movieId);

    Page<Showtime> findByRoom_Id(Long roomId, Pageable pageable);

    List<Showtime> findByRoom_Id(Long roomId);

    Optional<Showtime> findByMovie_IdAndRoom_IdAndStartTimeAndEndTime(Long movie_id, Long room_id,
            LocalDateTime startTime, LocalDateTime endTime);

    Boolean existsByRoom_IdAndStartTimeAndEndTime(Long room_id, LocalDateTime startTime, LocalDateTime endTime);

    // Detect any overlap where existing.startTime < newEnd AND existing.endTime >
    // newStart
    Boolean existsByRoom_IdAndStartTimeLessThanAndEndTimeGreaterThan(Long room_id, LocalDateTime endTime,
            LocalDateTime startTime);
}
