package com.example.cinemacommandservice.dto;

import java.util.List;

public record BuyTicketRequest(Long screeningId, List<Long> seatIds) {}