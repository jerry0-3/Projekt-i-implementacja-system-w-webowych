package com.example.cinemacommandservice.service;

import com.example.cinemacommandservice.dto.SeatInfoDto;
import com.example.cinemacommandservice.dto.TicketDetailsResponse;
import com.example.cinemacommandservice.model.*;
import com.example.cinemacommandservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final ReservedSeatRepository reservedSeatRepository;

    @Transactional
    public String buyTicket(Long screeningId, List<Long> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Musisz wybrać co najmniej jedno miejsce.");
        }

        // Weryfikacja czy KTÓREKOLWIEK z wybranych miejsc nie jest już zajęte
        if (reservedSeatRepository.existsByScreeningIdAndSeatIdIn(screeningId, seatIds)) {
            throw new IllegalStateException("Jedno lub więcej z wybranych miejsc jest już zajęte.");
        }

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new IllegalArgumentException("Seans nie istnieje"));

        List<Seat> selectedSeats = seatRepository.findAllById(seatIds);
        if (selectedSeats.size() != seatIds.size()) {
            throw new IllegalArgumentException("Jedno lub więcej wskazanych miejsc nie istnieje w systemie.");
        }

        Ticket ticket = Ticket.builder()
                .uniqueCode(UUID.randomUUID().toString())
                .screening(screening)
                .status(TicketStatus.VALID)
                .build();

        // Tworzenie i przypisywanie rezerwacji konkretnych foteli
        for (Seat seat : selectedSeats) {
            ReservedSeat reservedSeat = ReservedSeat.builder()
                    .screening(screening)
                    .seat(seat)
                    .build();
            ticket.addReservedSeat(reservedSeat);
        }

        ticketRepository.save(ticket);

        return ticket.getUniqueCode();
    }

    @Transactional(readOnly = true)
    public TicketDetailsResponse checkTicket(String uniqueCode) {
        Optional<Ticket> ticketOpt = ticketRepository.findByUniqueCodeWithDetails(uniqueCode);

        // Stan: Nie istnieje
        if (ticketOpt.isEmpty()) {
            return new TicketDetailsResponse("NIE ISTNIEJE", null, null, null, null);
        }

        Ticket ticket = ticketOpt.get();
        String dynamicStatus = calculateDynamicStatus(ticket);

        List<SeatInfoDto> seatInfos = ticket.getReservedSeats().stream()
                .map(rs -> new SeatInfoDto(rs.getSeat().getRowNumber(), rs.getSeat().getSeatNumber())).toList();

        String roomName = ticket.getReservedSeats().getFirst().getSeat().getRoom().getName();

        return new TicketDetailsResponse(dynamicStatus, seatInfos.size(), roomName,
                ticket.getScreening().getMovie().getTitle(), seatInfos);
    }

    @Transactional
    public void consumeTicket(String uniqueCode) {
        Ticket ticket = ticketRepository.findByUniqueCodeWithDetails(uniqueCode)
                .orElseThrow(() -> new IllegalArgumentException("Bilet nie istnieje"));

        String currentStatus = calculateDynamicStatus(ticket);

        if (!"WAŻNY".equals(currentStatus)) {
            throw new IllegalStateException("Nie można skasować biletu. Aktualny status: " + currentStatus);
        }

        ticket.setStatus(TicketStatus.SCANNED);
        ticketRepository.save(ticket);
    }

    // Prywatna metoda pomocnicza do wyliczania faktycznego stanu biletu w czasie rzeczywistym
    private String calculateDynamicStatus(Ticket ticket) {
        if (ticket.getStatus() == TicketStatus.SCANNED) {
            return "SKASOWANY";
        }
        if (ticket.getStatus() == TicketStatus.INVALID) {
            return "NIEWAŻNY"; // Nieważny ze względu na anulowanie w systemie
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validFrom = ticket.getScreening().getStartTime().minusMinutes(15);
        LocalDateTime validTo = ticket.getScreening().getEndTime();

        // Nieważny ze względu na czas
        if (now.isBefore(validFrom) || now.isAfter(validTo)) {
            return "NIEWAŻNY";
        }

        return "WAŻNY";
    }
}