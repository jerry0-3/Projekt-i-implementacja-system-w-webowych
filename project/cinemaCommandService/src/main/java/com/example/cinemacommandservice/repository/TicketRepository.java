package com.example.cinemacommandservice.repository;

import com.example.cinemacommandservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.reservationCode = :code")
    Optional<Ticket> findByReservationCode(@Param("code") String code);
}