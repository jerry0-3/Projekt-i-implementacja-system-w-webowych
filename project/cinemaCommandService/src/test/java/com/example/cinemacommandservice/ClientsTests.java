package com.example.cinemacommandservice;

import com.example.cinemacommandservice.client.QueryServiceClient;
import com.example.cinemacommandservice.dto.SyncReservationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

public class ClientsTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private QueryServiceClient queryServiceClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void syncReservedSeats_shouldCallRestTemplate() {
        Long screeningId = 1L;
        List<Long> seatIds = List.of(1L, 2L);

        queryServiceClient.syncReservedSeats(screeningId, seatIds);

        verify(restTemplate).postForEntity(
                eq("http://localhost:8081/api/internal/sync/reservation"),
                argThat(request -> {
                    SyncReservationDto dto = (SyncReservationDto) request;
                    return dto.screeningId().equals(screeningId)
                            && dto.seatIds().equals(seatIds);
                }),
                eq(Void.class)
        );
    }

    @Test
    void syncReservedSeats_shouldNotThrow_whenRestTemplateFails() {
        Long screeningId = 1L;
        List<Long> seatIds = List.of(1L);

        doThrow(new RuntimeException("Connection error"))
                .when(restTemplate)
                .postForEntity(anyString(), any(), eq(Void.class));

        // Should NOT throw
        queryServiceClient.syncReservedSeats(screeningId, seatIds);

        verify(restTemplate).postForEntity(anyString(), any(), eq(Void.class));
    }

    @Test
    void syncReservedSeats_shouldHandleEmptySeatList() {
        Long screeningId = 1L;
        List<Long> seatIds = List.of();

        queryServiceClient.syncReservedSeats(screeningId, seatIds);

        verify(restTemplate).postForEntity(
                anyString(),
                argThat(request -> ((SyncReservationDto) request).seatIds().isEmpty()),
                eq(Void.class)
        );
    }

    @Test
    void syncReservedSeats_shouldHandleNullSeatList() {
        Long screeningId = 1L;

        queryServiceClient.syncReservedSeats(screeningId, null);

        verify(restTemplate).postForEntity(
                anyString(),
                argThat(request -> ((SyncReservationDto) request).seatIds() == null),
                eq(Void.class)
        );
    }
}
