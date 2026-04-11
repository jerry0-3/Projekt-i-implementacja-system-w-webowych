package com.example.cinemacommandservice.repository;

import com.example.cinemacommandservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {}
