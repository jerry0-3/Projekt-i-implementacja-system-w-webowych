package com.piisw.jpa.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// Aktywuje automatyczne wypełnianie dat przez Spring Data JPA
@EntityListeners(AuditingEntityListener.class)
// Nadpisuje zapytanie DELETE na UPDATE (soft delete). Używamy version, bo mamy Optimistic Locking.
@SQLDelete(sql = "UPDATE server SET is_active = false WHERE id = ? AND version = ?")
// Automatycznie dodaje warunek do każdego SELECT-a, aby nie zwracać usuniętych serwerów
@SQLRestriction("is_active = true")
public class Server {

    @Id
    @SequenceGenerator(name = "SERVER_ID_GENERATOR", sequenceName = "SERVER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SERVER_ID_GENERATOR")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String ip;

    // Optimistic Locking
    @Version
    private Long version;

    // Data utworzenia (nie może być aktualizowana)
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    // Data ostatniej modyfikacji
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

    // Flaga do Soft Delete
    @Column(nullable = false)
    private boolean isActive = true;

    public Server(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }
}
