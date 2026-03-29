package com.piisw.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_ID_GENERATOR")
    @SequenceGenerator(name = "COMMENT_ID_GENERATOR", sequenceName = "COMMENT_SEQ", allocationSize = 1)
    private Long id;

    private String content;

    // Wiele komentarzy należy do jednego zdarzenia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Relacja M:N z Follower (kierunek zwrotny)
    @ManyToMany(mappedBy = "comments")
    private Set<Follower> followers;
}