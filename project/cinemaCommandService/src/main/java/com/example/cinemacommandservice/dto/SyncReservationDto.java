package com.example.cinemacommandservice.dto;

import java.util.List;

public record SyncReservationDto(Long screeningId, List<Long> seatIds) {}