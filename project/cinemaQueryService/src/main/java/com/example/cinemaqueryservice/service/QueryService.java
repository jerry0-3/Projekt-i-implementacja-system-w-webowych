package com.example.cinemaqueryservice.service;

import com.example.cinemaqueryservice.repository.*;
import com.example.cinemaqueryservice.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryService {

    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;

    public QueryService(MovieRepository movieRepository,
                        RoomRepository roomRepository,
                        ScreeningRepository screeningRepository) {
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.screeningRepository = screeningRepository;
    }

    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(f -> new MovieDTO(f.getId(), f.getTitle(), f.getDescription(),
                        f.getImageUrl(), f.getDurationMinutes()))
                .collect(Collectors.toList());
    }

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(r -> new RoomDTO(r.getId(), r.getRoomNumber(), r.getSeats()))
                .collect(Collectors.toList());
    }

    public List<ScreeningDTO> getAllScreenings() {
        return screeningRepository.findAll()
                .stream()
                .map(s -> new ScreeningDTO(s.getId(), s.getMovie(), s.getRoom(),
                        s.getStartTime(), s.getEndTime()))
                .collect(Collectors.toList());
    }

    public List<ScreeningDTO> getScreeningsByMovie(Long movieId) {
        return screeningRepository.findByMovieId(movieId)
                .stream()
                .map(s -> new ScreeningDTO(s.getId(), s.getMovie(), s.getRoom(),
                        s.getStartTime(), s.getEndTime()))
                .collect(Collectors.toList());
    }

    public List<ScreeningDTO> getScreeningsByRoom(Long roomId) {
        return screeningRepository.findByRoomId(roomId)
                .stream()
                .map(s -> new ScreeningDTO(s.getId(), s.getMovie(), s.getRoom(),
                        s.getStartTime(), s.getEndTime()))
                .collect(Collectors.toList());
    }
}
