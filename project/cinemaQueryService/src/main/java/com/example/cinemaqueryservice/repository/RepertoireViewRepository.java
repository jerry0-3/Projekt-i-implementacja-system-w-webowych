package com.example.cinemaqueryservice.repository;

import com.example.cinemaqueryservice.model.RepertoireView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepertoireViewRepository extends JpaRepository<RepertoireView, Long> {

    @Query("SELECT r FROM RepertoireView r WHERE " +
            "(:movieId IS NULL OR r.movieId = :movieId) AND " +
            "(:roomName IS NULL OR LOWER(r.roomName) LIKE LOWER(CONCAT('%', :roomName, '%')))")
    Page<RepertoireView> findFilteredRepertoire(@Param("movieId") Long movieId, @Param("roomName") String roomName, Pageable pageable);
}