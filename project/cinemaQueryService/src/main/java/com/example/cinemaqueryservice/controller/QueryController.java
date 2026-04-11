package com.example.cinemaqueryservice.controller;

import com.example.cinemaqueryservice.dto.*;
import com.example.cinemaqueryservice.service.QueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/movies")
    public List<MovieDTO> getMovies() {
        return queryService.getAllMovies();
    }

    @GetMapping("/rooms")
    public List<RoomDTO> getRooms() {
        return queryService.getAllRooms();
    }

    @GetMapping("/screenings")
    public List<ScreeningDTO> getScreenings() {
        return queryService.getAllScreenings();
    }

    @GetMapping("/screenings/film/{movieId}")
    public List<ScreeningDTO> getScreeningsByMovie(@PathVariable Long movieId) {
        return queryService.getScreeningsByMovie(movieId);
    }

    @GetMapping("/screenings/film/{roomId}")
    public List<ScreeningDTO> getScreeningsByRoom(@PathVariable Long roomId) {
        return queryService.getScreeningsByRoom(roomId);
    }
}
