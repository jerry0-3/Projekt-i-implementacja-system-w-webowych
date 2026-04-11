package com.example.cinemacommandservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatInfoDto {
    private Integer rowNumber;
    private Integer seatNumber;
}