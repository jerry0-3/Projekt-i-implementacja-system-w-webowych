package com.example.cinemacommandservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuyTicketResponse {
    private String uniqueCode;
    private String message;
}