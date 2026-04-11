package com.example.cinemacommandservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "movie_sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long movieId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String reservedSeats;

    @Version
    private Long version;
}
