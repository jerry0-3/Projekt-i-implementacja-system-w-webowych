package com.example.cinemaqueryservice.service;

import com.example.cinemaqueryservice.dto.MovieDictionaryDto;
import com.example.cinemaqueryservice.model.RepertoireView;
import com.example.cinemaqueryservice.model.ScreeningSeatView;
import com.example.cinemaqueryservice.repository.RepertoireViewRepository;
import com.example.cinemaqueryservice.repository.ScreeningSeatViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepertoireQueryService {

    private final RepertoireViewRepository repertoireRepository;
    private final ScreeningSeatViewRepository seatViewRepository;

    @Transactional(readOnly = true)
    public Page<RepertoireView> getRepertoire(Long movieId, String roomName, Pageable pageable) {
        return repertoireRepository.findFilteredRepertoire(movieId, roomName, pageable);
    }

    @Transactional(readOnly = true)
    public List<ScreeningSeatView> getSeatsForScreening(Long screeningId) {
        return seatViewRepository.findByScreeningId(screeningId);
    }

    @Transactional(readOnly = true)
    public List<MovieDictionaryDto> getAvailableMovies() {
        return repertoireRepository.findDistinctMovies();
    }

    @Transactional(readOnly = true)
    public List<String> getAvailableRooms() {
        return repertoireRepository.findDistinctRooms();
    }

    // Metoda wywoływana TYLKO przez wewnętrzny endpoint synchronizacyjny
    @Transactional
    public void syncReservedSeats(Long screeningId, List<Long> seatIds) {
        int updatedRows = seatViewRepository.markSeatsAsOccupied(screeningId, seatIds);

        log.info("Zsynchronizowano rezerwację dla seansu ID: {}. Zaktualizowano miejsc: {}",
                screeningId, updatedRows);

        if (updatedRows != seatIds.size()) {
            log.warn("Uwaga: Próbowano zaktualizować {} miejsc, ale w bazie zmieniono tylko {} rekordów.",
                    seatIds.size(), updatedRows);
        }
    }
}