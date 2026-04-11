package com.example.cinemaqueryservice.repository;

import com.example.cinemaqueryservice.model.ScreeningSeatView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScreeningSeatViewRepository extends JpaRepository<ScreeningSeatView, Long> {

    List<ScreeningSeatView> findByScreeningId(Long screeningId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE ScreeningSeatView s SET s.isAvailable = false WHERE s.screeningId = :screeningId AND s.originalSeatId IN :seatIds")
    int markSeatsAsOccupied(@Param("screeningId") Long screeningId, @Param("seatIds") List<Long> seatIds);
}