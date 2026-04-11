package com.example.cinemaqueryservice.dto;

import com.example.cinemaqueryservice.model.Movie;
import com.example.cinemaqueryservice.model.Room;

import java.time.LocalDateTime;

public record ScreeningDTO(Long id, Movie movie, Room room, LocalDateTime startTime, LocalDateTime endTime) {}
