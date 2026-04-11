package com.example.cinemacommandservice.repository;

import com.example.cinemacommandservice.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {}
