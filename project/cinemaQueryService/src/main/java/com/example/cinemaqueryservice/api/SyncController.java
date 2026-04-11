package com.example.cinemaqueryservice.api;

import com.example.cinemaqueryservice.dto.SyncReservationDto;
import com.example.cinemaqueryservice.service.RepertoireQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/sync")
@RequiredArgsConstructor
@Tag(name = "Synchronizacja (Internal)", description = "Tylko do użytku wewnętrznego przez Command Service")
public class SyncController {

    private final RepertoireQueryService queryService;

    @PostMapping("/reservation")
    @Operation(summary = "Zaktualizuj widok miejsc", description = "Oznacza podane miejsca jako zajęte. Wywoływane przez Command Service.")
    public ResponseEntity<Void> syncReservedSeats(@RequestBody SyncReservationDto request) {

        queryService.syncReservedSeats(request.screeningId(), request.seatIds());

        return ResponseEntity.ok().build();
    }
}