package com.example.cinemacommandservice;

import com.example.cinemacommandservice.client.QueryServiceClient;
import com.example.cinemacommandservice.dto.TicketDetailsResponse;
import com.example.cinemacommandservice.model.*;
import com.example.cinemacommandservice.repository.*;
import com.example.cinemacommandservice.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicesTests {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ReservedSeatRepository reservedSeatRepository;
    @Mock
    private QueryServiceClient queryServiceClient;

    @InjectMocks
    private TicketService ticketService;

    private Screening screening;
    private Seat seat1;
    private Seat seat2;

    @BeforeEach
    void setup() {
        screening = Screening.builder()
                .id(1L)
                .startTime(LocalDateTime.now().minusMinutes(5))
                .endTime(LocalDateTime.now().plusHours(2))
                .movie(Movie.builder().title("Test Movie").build())
                .build();

        Room room = Room.builder().name("Room A").build();

        seat1 = Seat.builder().id(1L).rowNumber(1).seatNumber(1).room(room).build();
        seat2 = Seat.builder().id(2L).rowNumber(1).seatNumber(2).room(room).build();
    }

    @Test
    void buyTicket_shouldSucceed() {
        List<Long> seatIds = List.of(1L, 2L);

        when(reservedSeatRepository.existsByScreeningIdAndSeatIdIn(1L, seatIds)).thenReturn(false);
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));
        when(seatRepository.findAllById(seatIds)).thenReturn(List.of(seat1, seat2));

        String result = ticketService.buyTicket(1L, seatIds);

        assertNotNull(result);
        verify(ticketRepository).save(any(Ticket.class));
        verify(queryServiceClient).syncReservedSeats(1L, seatIds);
    }

    @Test
    void buyTicket_shouldThrow_whenSeatIdsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> ticketService.buyTicket(1L, List.of()));
    }

    @Test
    void buyTicket_shouldThrow_whenSeatIdsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> ticketService.buyTicket(1L, null));
    }

    @Test
    void buyTicket_shouldThrow_whenSeatAlreadyReserved() {
        List<Long> seatIds = List.of(1L);

        when(reservedSeatRepository.existsByScreeningIdAndSeatIdIn(1L, seatIds))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> ticketService.buyTicket(1L, seatIds));
    }

    @Test
    void buyTicket_shouldThrow_whenScreeningNotFound() {
        List<Long> seatIds = List.of(1L);

        when(reservedSeatRepository.existsByScreeningIdAndSeatIdIn(1L, seatIds)).thenReturn(false);
        when(screeningRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> ticketService.buyTicket(1L, seatIds));
    }

    @Test
    void buyTicket_shouldThrow_whenSeatNotFound() {
        List<Long> seatIds = List.of(1L, 2L);

        when(reservedSeatRepository.existsByScreeningIdAndSeatIdIn(1L, seatIds)).thenReturn(false);
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));
        when(seatRepository.findAllById(seatIds)).thenReturn(List.of(seat1)); // missing one

        assertThrows(IllegalArgumentException.class,
                () -> ticketService.buyTicket(1L, seatIds));
    }

    @Test
    void checkTicket_shouldReturnNotExists() {
        when(ticketRepository.findByUniqueCodeWithDetails("code"))
                .thenReturn(Optional.empty());

        TicketDetailsResponse response = ticketService.checkTicket("code");

        assertEquals("NIE ISTNIEJE", response.status());
        assertNull(response.totalSeats());
    }

    @Test
    void checkTicket_shouldReturnValid() {
        Ticket ticket = buildTicket(TicketStatus.VALID);

        when(ticketRepository.findByUniqueCodeWithDetails("code"))
                .thenReturn(Optional.of(ticket));

        TicketDetailsResponse response = ticketService.checkTicket("code");

        assertEquals("WAŻNY", response.status());
        assertEquals(2, response.totalSeats());
        assertEquals("Room A", response.roomName());
    }

    @Test
    void checkTicket_shouldReturnScanned() {
        Ticket ticket = buildTicket(TicketStatus.SCANNED);

        when(ticketRepository.findByUniqueCodeWithDetails("code"))
                .thenReturn(Optional.of(ticket));

        TicketDetailsResponse response = ticketService.checkTicket("code");

        assertEquals("SKASOWANY", response.status());
    }

    @Test
    void checkTicket_shouldReturnInvalid_dueToTime() {
        screening.setStartTime(LocalDateTime.now().plusHours(1)); // future

        Ticket ticket = buildTicket(TicketStatus.VALID);

        when(ticketRepository.findByUniqueCodeWithDetails("code"))
                .thenReturn(Optional.of(ticket));

        TicketDetailsResponse response = ticketService.checkTicket("code");

        assertEquals("NIEWAŻNY", response.status());
    }

    @Test
    void consumeTicket_shouldSucceed() {
        Ticket ticket = buildTicket(TicketStatus.VALID);

        when(ticketRepository.findByUniqueCodeWithDetails("code"))
                .thenReturn(Optional.of(ticket));

        ticketService.consumeTicket("code");

        assertEquals(TicketStatus.SCANNED, ticket.getStatus());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void consumeTicket_shouldThrow_whenNotFound() {
        when(ticketRepository.findByUniqueCodeWithDetails("code"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> ticketService.consumeTicket("code"));
    }

    @Test
    void consumeTicket_shouldThrow_whenAlreadyScanned() {
        Ticket ticket = buildTicket(TicketStatus.SCANNED);

        when(ticketRepository.findByUniqueCodeWithDetails("code"))
                .thenReturn(Optional.of(ticket));

        assertThrows(IllegalStateException.class,
                () -> ticketService.consumeTicket("code"));
    }

    @Test
    void consumeTicket_shouldThrow_whenExpired() {
        screening.setEndTime(LocalDateTime.now().minusHours(1));

        Ticket ticket = buildTicket(TicketStatus.VALID);

        when(ticketRepository.findByUniqueCodeWithDetails("code"))
                .thenReturn(Optional.of(ticket));

        assertThrows(IllegalStateException.class,
                () -> ticketService.consumeTicket("code"));
    }

    private Ticket buildTicket(TicketStatus status) {
        Ticket ticket = Ticket.builder()
                .uniqueCode("code")
                .status(status)
                .screening(screening)
                .build();

        ReservedSeat rs1 = ReservedSeat.builder().seat(seat1).screening(screening).build();
        ReservedSeat rs2 = ReservedSeat.builder().seat(seat2).screening(screening).build();

        ticket.addReservedSeat(rs1);
        ticket.addReservedSeat(rs2);

        return ticket;
    }
}
