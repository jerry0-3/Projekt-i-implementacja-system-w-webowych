package com.example.cinemaqueryservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "screenings")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Movie movie;
    @OneToOne
    private Room room;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
