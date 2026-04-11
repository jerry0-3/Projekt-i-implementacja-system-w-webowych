package com.example.cinemacommandservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reservationCode;
    private Long sessionId;
    private String bookedSeats;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Version
    private Long version;
}
