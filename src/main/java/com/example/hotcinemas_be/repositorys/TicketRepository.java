package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
