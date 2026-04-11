package com.example.cinemacommandservice.repository;

import com.example.cinemacommandservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Używamy JOIN FETCH dla reservedSeats i ich seat, aby uniknąć problemu N+1
    @Query("SELECT DISTINCT t FROM Ticket t " +
            "JOIN FETCH t.screening s " +
            "JOIN FETCH t.reservedSeats rs " +
            "JOIN FETCH rs.seat " +
            "WHERE t.uniqueCode = :code")
    Optional<Ticket> findByUniqueCodeWithDetails(@Param("code") String code);
}