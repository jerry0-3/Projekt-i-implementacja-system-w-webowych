package com.example.cinemacommandservice;

import com.example.cinemacommandservice.model.Ticket;
import com.example.cinemacommandservice.repository.ReservedSeatRepository;
import com.example.cinemacommandservice.repository.ScreeningRepository;
import com.example.cinemacommandservice.repository.SeatRepository;
import com.example.cinemacommandservice.repository.TicketRepository;
import com.example.cinemacommandservice.service.TicketService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RepositoriesTests {

    @Autowired
    private ReservedSeatRepository reservedSeatRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldExistsByScreeningIdAndSeatIdIn(){
        Long screeningID = 1L;
        List<Long> seatsIDs = new ArrayList();
        seatsIDs.add(1L);

        ticketService.buyTicket(screeningID,seatsIDs);

        assertThat(
            reservedSeatRepository.existsByScreeningIdAndSeatIdIn(screeningID,seatsIDs),
            is(true)
        );
    }

    @Test
    void shouldNotExistsReservedSeatByDefault(){
        Long screeningID = 1L;
        List<Long> seatsIDs = new ArrayList();
        seatsIDs.add(1L);

        assertThat(
                reservedSeatRepository.existsByScreeningIdAndSeatIdIn(screeningID,seatsIDs),
                is(false)
        );
    }

    @Test
    void shouldFindByUniqueCodeWithDetails(){
        Long screeningID = 1L;
        List<Long> seatsIDs = new ArrayList();
        seatsIDs.add(1L);

        String ticketCode = ticketService.buyTicket(screeningID,seatsIDs);

        Optional<Ticket> ticket = ticketRepository.findByUniqueCodeWithDetails(ticketCode);

        assertThat(ticket.isPresent(), is(true));
    }

    @Test
    void shouldNotFindTicketByWrongCode(){
        String ticketCode = new String("");

        Optional<Ticket> ticket = ticketRepository.findByUniqueCodeWithDetails(ticketCode);

        assertThat(ticket.isPresent(), is(false));
    }

}
