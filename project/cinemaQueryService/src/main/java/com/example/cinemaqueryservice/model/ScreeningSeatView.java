package com.example.cinemaqueryservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
// Indeks przyspieszy wyszukiwanie miejsc dla konkretnego seansu
@Table(name = "screening_seats_view", indexes = {
        @Index(name = "idx_screening_id", columnList = "screeningId")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ScreeningSeatView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long screeningId;
    private Long originalSeatId; // ID miejsca, które odeślemy do CommandService przy zakupie

    private Integer rowNumber;
    private Integer seatNumber;

    // Najważniejsza flaga dla widza!
    private boolean isAvailable;
}