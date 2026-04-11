package com.example.cinemacommandservice.dto;

import java.util.List;

public record TicketDetailsResponse(String status, Integer totalSeats, String roomName, String movieTitle, List<SeatInfoDto> seats) {}