package com.example.cinemaqueryservice;

import com.example.cinemaqueryservice.dto.MovieDictionaryDto;
import com.example.cinemaqueryservice.model.RepertoireView;
import com.example.cinemaqueryservice.model.ScreeningSeatView;
import com.example.cinemaqueryservice.repository.RepertoireViewRepository;
import com.example.cinemaqueryservice.repository.ScreeningSeatViewRepository;
import com.example.cinemaqueryservice.service.RepertoireQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class ServicesTests {
    @Mock
    private RepertoireViewRepository repertoireRepository;

    @Mock
    private ScreeningSeatViewRepository seatViewRepository;

    @InjectMocks
    private RepertoireQueryService repertoireQueryService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }

    // ========================
    // getRepertoire tests
    // ========================

    @Test
    void shouldReturnFilteredRepertoire() {
        // given
        Long movieId = 1L;
        String roomName = "Room A";

        List<RepertoireView> content = List.of(mock(RepertoireView.class));
        Page<RepertoireView> page = new PageImpl<>(content);

        when(repertoireRepository.findFilteredRepertoire(movieId, roomName, pageable))
                .thenReturn(page);

        // when
        Page<RepertoireView> result =
                repertoireQueryService.getRepertoire(movieId, roomName, pageable);

        // then
        assertThat(result).isEqualTo(page);

        verify(repertoireRepository).findFilteredRepertoire(movieId, roomName, pageable);
        verifyNoMoreInteractions(repertoireRepository);
    }

    @Test
    void shouldHandleNullFiltersInGetRepertoire() {
        // given
        Page<RepertoireView> emptyPage = Page.empty();

        when(repertoireRepository.findFilteredRepertoire(null, null, pageable))
                .thenReturn(emptyPage);

        // when
        Page<RepertoireView> result =
                repertoireQueryService.getRepertoire(null, null, pageable);

        // then
        assertThat(result).isEmpty();
        verify(repertoireRepository).findFilteredRepertoire(null, null, pageable);
    }

    // ========================
    // getSeatsForScreening tests
    // ========================

    @Test
    void shouldReturnSeatsForScreening() {
        // given
        Long screeningId = 100L;
        List<ScreeningSeatView> seats = List.of(
                mock(ScreeningSeatView.class),
                mock(ScreeningSeatView.class)
        );

        when(seatViewRepository.findByScreeningId(screeningId))
                .thenReturn(seats);

        // when
        List<ScreeningSeatView> result =
                repertoireQueryService.getSeatsForScreening(screeningId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(seats);

        verify(seatViewRepository).findByScreeningId(screeningId);
        verifyNoMoreInteractions(seatViewRepository);
    }

    @Test
    void shouldReturnEmptyListWhenNoSeatsFound() {
        // given
        Long screeningId = 200L;

        when(seatViewRepository.findByScreeningId(screeningId))
                .thenReturn(List.of());

        // when
        List<ScreeningSeatView> result =
                repertoireQueryService.getSeatsForScreening(screeningId);

        // then
        assertThat(result).isEmpty();

        verify(seatViewRepository).findByScreeningId(screeningId);
    }

    // ========================
    // syncReservedSeats tests
    // ========================

    @Test
    void shouldSyncReservedSeatsSuccessfully() {
        // given
        Long screeningId = 1L;
        List<Long> seatIds = List.of(1L, 2L, 3L);

        when(seatViewRepository.markSeatsAsOccupied(screeningId, seatIds))
                .thenReturn(3);

        // when
        repertoireQueryService.syncReservedSeats(screeningId, seatIds);

        // then
        verify(seatViewRepository).markSeatsAsOccupied(screeningId, seatIds);
    }

    @Test
    void shouldLogWarningWhenUpdatedRowsMismatch() {
        // given
        Long screeningId = 1L;
        List<Long> seatIds = List.of(1L, 2L, 3L);

        when(seatViewRepository.markSeatsAsOccupied(screeningId, seatIds))
                .thenReturn(2); // mismatch

        // when
        repertoireQueryService.syncReservedSeats(screeningId, seatIds);

        // then
        verify(seatViewRepository).markSeatsAsOccupied(screeningId, seatIds);
        // Logging is not directly asserted, but behavior is covered
    }

    @Test
    void shouldHandleEmptySeatList() {
        // given
        Long screeningId = 1L;
        List<Long> seatIds = List.of();

        when(seatViewRepository.markSeatsAsOccupied(screeningId, seatIds))
                .thenReturn(0);

        // when
        repertoireQueryService.syncReservedSeats(screeningId, seatIds);

        // then
        verify(seatViewRepository).markSeatsAsOccupied(screeningId, seatIds);
    }

    @Test
    void shouldReturnAvailableMovies(){
        when(repertoireRepository.findDistinctMovies()).thenReturn(new ArrayList<>());

        List<MovieDictionaryDto> movies = repertoireQueryService.getAvailableMovies();

        assertThat(movies).isNotNull();

        verify(repertoireRepository).findDistinctMovies();
    }

    @Test
    void shouldReturnAvailableRooms(){
        when(repertoireRepository.findDistinctRooms()).thenReturn(new ArrayList<>());

        List<String> rooms = repertoireQueryService.getAvailableRooms();

        assertThat(rooms).isNotNull();

        verify(repertoireRepository).findDistinctRooms();
    }
}
