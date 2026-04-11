package com.example.cinemaqueryservice.repository;

import com.example.cinemaqueryservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByMovieId(Long filmId);
    List<Screening> findByRoomId(Long roomId);
}
