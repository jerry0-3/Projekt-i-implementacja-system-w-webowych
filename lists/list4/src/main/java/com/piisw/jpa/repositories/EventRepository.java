package com.piisw.jpa.repositories;

import com.piisw.jpa.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // --- ZADANIE 2 ---
    Page<Event> findByTimeBetweenAndAnalysisRequired(
            LocalDateTime start,
            LocalDateTime end,
            boolean analysisRequired,
            Pageable pageable
    );

    // --- ZADANIE 3 ---

    // 1. Bulk Delete
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Event e WHERE e.time < :timeThreshold")
    void deleteInBulkBeforDate(@Param("timeThreshold") LocalDateTime timeThreshold);

    // 2. Bulk Update
    // Używamy TYPE(e) aby filtrować rekordy należące tylko do określonej podklasy
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Event e SET e.analysisRequired = true WHERE TYPE(e) = :clazz AND e.duration > :minDuration")
    void updateInBulkToBeAnalyzedByType(
            @Param("clazz") Class<? extends Event> clazz,
            @Param("minDuration") int minDuration
    );

    // --- ZADANIE 4 ---

    // Używamy "Constructor Expression" podając pełną ścieżkę do klasy/rekordu
    @Query("SELECT new com.piisw.jpa.repositories.ServerStatistic(e.server, COUNT(e)) FROM Event e GROUP BY e.server")
    List<ServerStatistic> countEventsByServer();
}