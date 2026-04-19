package com.example.cinemacommandservice;

import com.example.cinemacommandservice.api.TicketCommandController;
import com.example.cinemacommandservice.config.RestTemplateConfig;
import com.example.cinemacommandservice.config.SecurityConfig;
import com.example.cinemacommandservice.dto.*;
import com.example.cinemacommandservice.exception.GlobalExceptionHandler;
import com.example.cinemacommandservice.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketCommandController.class)
@Import({RestTemplateConfig.class, SecurityConfig.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
public class ApiTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    // ========================
    // BUY TICKET
    // ========================

    @Test
    void buyTicket_shouldReturn201AndResponse() throws Exception {
        BuyTicketRequest request = new BuyTicketRequest(1L, List.of(1L, 2L));

        when(ticketService.buyTicket(1L, List.of(1L, 2L)))
                .thenReturn("ABC123");

        mockMvc.perform(post("/api/commands/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uniqueCode").value("ABC123"));
    }

    @Test
    void buyTicket_shouldReturn400_withMessage() throws Exception {
        when(ticketService.buyTicket(any(), any()))
                .thenThrow(new IllegalArgumentException("Niepoprawne dane"));

        BuyTicketRequest request = new BuyTicketRequest(1L, List.of());

        mockMvc.perform(post("/api/commands/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buyTicket_shouldReturn409_withMessage() throws Exception {
        when(ticketService.buyTicket(any(), any()))
                .thenThrow(new IllegalStateException("Miejsce zajęte"));

        BuyTicketRequest request = new BuyTicketRequest(1L, List.of(1L));

        mockMvc.perform(post("/api/commands/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void buyTicket_shouldHandleOptimisticLocking() throws Exception {
        when(ticketService.buyTicket(any(), any()))
                .thenThrow(new ObjectOptimisticLockingFailureException("Seat", 1L));

        BuyTicketRequest request = new BuyTicketRequest(1L, List.of(1L));

        mockMvc.perform(post("/api/commands/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    // ========================
    // CHECK TICKET
    // ========================

    @Test
    void checkTicket_shouldReturn200AndBody() throws Exception {
        TicketDetailsResponse response = new TicketDetailsResponse(
                "WAŻNY",
                2,
                "Room A",
                "Movie",
                List.of(new SeatInfoDto(1, 1))
        );

        when(ticketService.checkTicket("ABC123"))
                .thenReturn(response);

        mockMvc.perform(get("/api/commands/tickets/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WAŻNY"));
    }

    // ========================
    // CONSUME TICKET
    // ========================

    @Test
    void consumeTicket_shouldReturn200() throws Exception {
        mockMvc.perform(post("/api/commands/tickets/ABC123/consume"))
                .andExpect(status().isOk());

        verify(ticketService).consumeTicket("ABC123");
    }

    @Test
    void consumeTicket_shouldReturn400_withMessage() throws Exception {
        doThrow(new IllegalArgumentException("Bilet nie istnieje"))
                .when(ticketService).consumeTicket("ABC123");

        mockMvc.perform(post("/api/commands/tickets/ABC123/consume"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void consumeTicket_shouldReturn409_withMessage() throws Exception {
        doThrow(new IllegalStateException("Nie można skasować"))
                .when(ticketService).consumeTicket("ABC123");

        mockMvc.perform(post("/api/commands/tickets/ABC123/consume"))
                .andExpect(status().isConflict());
    }

    @Test
    void consumeTicket_shouldHandleOptimisticLocking() throws Exception {
        doThrow(new ObjectOptimisticLockingFailureException("Ticket", 1L))
                .when(ticketService).consumeTicket("ABC123");

        mockMvc.perform(post("/api/commands/tickets/ABC123/consume"))
                .andExpect(status().isConflict());
    }

    @TestConfiguration
    static class Config {
        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
