package com.example.cinemaqueryservice.repository;

import com.example.cinemaqueryservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
