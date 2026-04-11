package com.example.cinemaqueryservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "repertoire_view")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RepertoireView {

    @Id
    private Long screeningId; // Używamy ID seansu jako klucza głównego dla tego widoku

    // Płaskie dane o filmie
    private Long movieId;
    private String movieTitle;
    @Column(length = 1000)
    private String movieDescription;
    private String moviePhotoUrl;

    // Dane o seansie i sali
    private String roomName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
}