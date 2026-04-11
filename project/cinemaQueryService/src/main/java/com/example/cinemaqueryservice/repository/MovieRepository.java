package com.example.cinemaqueryservice.repository;

import com.example.cinemaqueryservice.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
