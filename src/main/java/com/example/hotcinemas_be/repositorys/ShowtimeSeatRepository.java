package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Showtime;
import com.example.hotcinemas_be.models.ShowtimeSeat;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, Long> {
    List<ShowtimeSeat> findByShowtime(Showtime showtime);

    List<ShowtimeSeat> findAllByShowtime_Id(Long showtimeShowtimeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ShowtimeSeat s where s.id = :id")
    Optional<ShowtimeSeat> findByIdForUpdate(@Param("id") Long id);

    @Modifying
    @Query("update ShowtimeSeat s set s.status = com.example.hotcinemas_be.enums.SeatStatus.AVAILABLE, s.heldByUser = null, s.heldUntil = null "
            +
            "where s.status = com.example.hotcinemas_be.enums.SeatStatus.HELD and s.heldUntil is not null and s.heldUntil < :now")
    int releaseExpiredHolds(@Param("now") LocalDateTime now);
}
