package com.example.cinemacommandservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
// KLUCZOWE: Zapobiega kupieniu dwóch biletów na to samo miejsce na ten sam seans
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"screening_id", "seat_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uniqueCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReservedSeat> reservedSeats = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    // Optimistic Locking — zapobiega jednoczesnej modyfikacji (np. podwójnemu skasowaniu)
    @Version
    private Long version;

    // Metoda pomocnicza do dodawania miejsc, aby zachować spójność relacji dwukierunkowej
    public void addReservedSeat(ReservedSeat reservedSeat) {
        reservedSeats.add(reservedSeat);
        reservedSeat.setTicket(this);
    }
}
