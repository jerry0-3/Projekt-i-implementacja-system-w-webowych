package com.example.cinemaqueryservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "screening_seats_view", indexes = {
        @Index(name = "idx_screening_id", columnList = "screeningId")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ScreeningSeatView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long screeningId;
    private Long originalSeatId;

    private Integer rowNumber;
    private Integer seatNumber;

    private boolean isAvailable;
}