package com.example.cinemaqueryservice;

import com.example.cinemaqueryservice.api.CinemaQueryController;
import com.example.cinemaqueryservice.api.SyncController;
import com.example.cinemaqueryservice.config.SecurityConfig;
import com.example.cinemaqueryservice.dto.SyncReservationDto;
import com.example.cinemaqueryservice.model.RepertoireView;
import com.example.cinemaqueryservice.model.ScreeningSeatView;
import com.example.cinemaqueryservice.service.RepertoireQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CinemaQueryController.class)
@Import(SecurityConfig.class)
public class ApiTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RepertoireQueryService queryService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SyncController syncController;

    // ========================
    // GET /repertoire
    // ========================

    @Test
    void shouldReturnPagedRepertoire() throws Exception {
        // given
        Page<RepertoireView> page = new PageImpl<>(
                List.of(new RepertoireView()),
                PageRequest.of(0, 10),
                1
        );

        when(queryService.getRepertoire(any(), any(), any(Pageable.class)))
                .thenReturn(page);

        // when + then
        mockMvc.perform(get("/api/queries/repertoire")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$.page").exists());
    }

    @Test
    void shouldPassFiltersToService() throws Exception {
        // given
        Page<RepertoireView> page = Page.empty();

        when(queryService.getRepertoire(eq(1L), eq("Room A"), any(Pageable.class)))
                .thenReturn(page);

        // when + then
        mockMvc.perform(get("/api/queries/repertoire")
                        .param("movieId", "1")
                        .param("roomName", "Room A")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void shouldHandlePaginationParameters() throws Exception {
        // given
        Pageable pageable = PageRequest.of(1, 5);

        Page<RepertoireView> page = new PageImpl<>(
                List.of(new RepertoireView(), new RepertoireView()),
                pageable,
                2
        );

        when(queryService.getRepertoire(any(), any(), any(Pageable.class)))
                .thenReturn(page);

        // when + then
        mockMvc.perform(get("/api/queries/repertoire")
                        .param("page", "1")
                        .param("size", "5")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(5))
                .andExpect(jsonPath("$.page.number").value(1));
    }

    @Test
    void shouldReturnEmptyPagedModelWhenNoResults() throws Exception {
        // given
        Page<RepertoireView> emptyPage = Page.empty();

        when(queryService.getRepertoire(any(), any(), any(Pageable.class)))
                .thenReturn(emptyPage);

        // when + then
        mockMvc.perform(get("/api/queries/repertoire")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(0));
    }

    // ========================
    // GET /screenings/{id}/seats
    // ========================

    @Test
    void shouldReturnSeatsForScreening() throws Exception {
        // given
        List<ScreeningSeatView> seats = List.of(
                new ScreeningSeatView(),
                new ScreeningSeatView()
        );

        when(queryService.getSeatsForScreening(1L))
                .thenReturn(seats);

        // when + then
        mockMvc.perform(get("/api/queries/screenings/1/seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturn404WhenNoSeatsFound() throws Exception {
        // given
        when(queryService.getSeatsForScreening(1L))
                .thenReturn(List.of());

        // when + then
        mockMvc.perform(get("/api/queries/screenings/1/seats"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldHandleDifferentScreeningId() throws Exception {
        // given
        when(queryService.getSeatsForScreening(999L))
                .thenReturn(List.of(new ScreeningSeatView()));

        // when + then
        mockMvc.perform(get("/api/queries/screenings/999/seats"))
                .andExpect(status().isOk());
    }

    // ========================
    // HAPPY PATH
    // ========================

    @Test
    void shouldSyncReservedSeatsSuccessfully() throws Exception {
        // given
        SyncReservationDto request = new SyncReservationDto(
                1L,
                List.of(10L, 20L, 30L)
        );

        // when + then
        mockMvc.perform(post("/api/internal/sync/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(syncController).syncReservedSeats(
                eq(new SyncReservationDto(1L, List.of(10L, 20L, 30L)))
        );
    }

    // ========================
    // EDGE CASES
    // ========================

    @Test
    void shouldHandleEmptySeatList() throws Exception {
        // given
        SyncReservationDto request = new SyncReservationDto(
                2L,
                List.of()
        );

        // when + then
        mockMvc.perform(post("/api/internal/sync/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(syncController).syncReservedSeats(
                eq(new SyncReservationDto(2L, List.of()))
        );
    }

    @Test
    void shouldHandleLargeSeatList() throws Exception {
        // given
        List<Long> seats = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

        SyncReservationDto request = new SyncReservationDto(
                99L,
                seats
        );

        // when + then
        mockMvc.perform(post("/api/internal/sync/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(syncController).syncReservedSeats(
                eq(new SyncReservationDto(99L, seats)));
    }

    // ========================
    // VALIDATION / BAD REQUESTS
    // ========================

    @Test
    void shouldReturn400WhenBodyIsMissing() throws Exception {
        mockMvc.perform(post("/api/internal/sync/reservation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenBodyIsInvalidJson() throws Exception {
        mockMvc.perform(post("/api/internal/sync/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json }"))
                .andExpect(status().isBadRequest());
    }
}
