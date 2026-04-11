package com.example.cinemacommandservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cinemacommandservice.model.ReservedSeat;
import java.util.List;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {
    boolean existsByScreeningIdAndSeatIdIn(Long screeningId, List<Long> seatIds);
}