package com.piisw.jpa.repositories;

import com.piisw.jpa.entities.Follower;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

    // Wymuszamy użycie grafu encji! Dzięki temu pobranie Follower-a z bazy
    // wykona JEDNO zapytanie z LEFT OUTER JOIN zamiast mnóstwa małych zapytań.
    @EntityGraph(value = "Follower.withCommentsAndEvents", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Follower> findByUserId(String userId);
}