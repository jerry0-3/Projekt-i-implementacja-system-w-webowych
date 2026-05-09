package com.example.cinemacommandservice.client;

import com.example.cinemacommandservice.dto.SyncReservationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueryServiceClient {

    private final RestTemplate restTemplate;

    private static final String QUERY_SERVICE_URL = "http://localhost:8081/api/internal/sync/reservation";

    public void syncReservedSeats(Long screeningId, List<Long> seatIds) {
        SyncReservationDto request = new SyncReservationDto(screeningId, seatIds);

        try {
            log.info("Próba synchronizacji rezerwacji dla seansu {} i miejsc {}", screeningId, seatIds);

            restTemplate.postForEntity(QUERY_SERVICE_URL, request, Void.class);

            log.info("Pomyślnie zsynchronizowano rezerwację z Query Service.");
        } catch (Exception e) {
            log.error("Nie udało się zsynchronizować rezerwacji z Query Service. Powód: {}", e.getMessage());
        }
    }
}