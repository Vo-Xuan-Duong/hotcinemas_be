package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
