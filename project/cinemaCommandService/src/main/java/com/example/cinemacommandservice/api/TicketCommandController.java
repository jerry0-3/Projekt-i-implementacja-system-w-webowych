package com.example.cinemacommandservice.api;

import com.example.cinemacommandservice.dto.BuyTicketRequest;
import com.example.cinemacommandservice.dto.BuyTicketResponse;
import com.example.cinemacommandservice.dto.TicketDetailsResponse;
import com.example.cinemacommandservice.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commands/tickets")
@RequiredArgsConstructor
@Tag(name = "Zarządzanie Biletami (Command)", description = "Endpoint-y do kupowania i kasowania biletów")
public class TicketCommandController {

    private final TicketService ticketService;

    @PostMapping
    @Operation(summary = "Kup bilet", description = "Generuje nowy bilet dla widza na konkretny seans i miejsce.")
    public ResponseEntity<BuyTicketResponse> buyTicket(@RequestBody BuyTicketRequest request) {
        String ticketCode = ticketService.buyTicket(request.getScreeningId(), request.getSeatIds());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BuyTicketResponse(ticketCode, "Bilet został pomyślnie zakupiony."));
    }

    @GetMapping("/{uniqueCode}")
    @Operation(summary = "Sprawdź bilet (Skanuj)", description = "Pobiera szczegóły biletu oraz jego aktualny status (WAŻNY, SKASOWANY, NIEWAŻNY, NIE ISTNIEJE).")
    public ResponseEntity<TicketDetailsResponse> checkTicket(@PathVariable String uniqueCode) {
        TicketDetailsResponse response = ticketService.checkTicket(uniqueCode);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{uniqueCode}/consume")
    @Operation(summary = "Skasuj bilet", description = "Zmienia status biletu na SKASOWANY. Możliwe tylko, jeśli bilet ma status WAŻNY.")
    public ResponseEntity<Void> consumeTicket(@PathVariable String uniqueCode) {
        ticketService.consumeTicket(uniqueCode);
        return ResponseEntity.ok().build();
    }
}