package com.example.cinemacommandservice.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TicketDetailsResponse {
    private String status;
    private Integer totalSeats;
    private String roomName;
    private String movieTitle;
    private List<SeatInfoDto> seats;
}