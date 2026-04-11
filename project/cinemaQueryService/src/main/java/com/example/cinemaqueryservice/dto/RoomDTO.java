package com.example.cinemaqueryservice.dto;

import com.example.cinemaqueryservice.model.Seat;

import java.util.List;

public record RoomDTO(Long id, int roomNumber, List<Seat> seats) {}
