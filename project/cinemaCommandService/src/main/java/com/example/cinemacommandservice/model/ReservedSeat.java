package com.example.cinemacommandservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reserved_seats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"screening_id", "seat_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservedSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;
}