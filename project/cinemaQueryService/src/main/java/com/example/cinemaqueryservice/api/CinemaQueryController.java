package com.example.cinemaqueryservice.api;

import com.example.cinemaqueryservice.model.RepertoireView;
import com.example.cinemaqueryservice.model.ScreeningSeatView;
import com.example.cinemaqueryservice.service.RepertoireQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queries")
@RequiredArgsConstructor
@Tag(name = "Przeglądanie Kina (Query)", description = "Endpointy do odczytu repertuaru i sprawdzania wolnych miejsc")
public class CinemaQueryController {

    private final RepertoireQueryService queryService;

    @GetMapping(value = "/repertoire", produces = "application/hal+json")
    @Operation(summary = "Pobierz repertuar", description = "Zwraca zdenormalizowaną listę seansów. Obsługuje opcjonalne filtrowanie i paginację.")
    public ResponseEntity<PagedModel<EntityModel<RepertoireView>>> getRepertoire(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String roomName,
            Pageable pageable,
            PagedResourcesAssembler<RepertoireView> pagedAssembler) {

        Page<RepertoireView> repertoirePage = queryService.getRepertoire(movieId, roomName, pageable);

        PagedModel<EntityModel<RepertoireView>> pagedModel = pagedAssembler.toModel(repertoirePage);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/screenings/{screeningId}/seats")
    @Operation(summary = "Pobierz plan sali", description = "Zwraca listę wszystkich miejsc dla danego seansu wraz z informacją, czy są wolne.")
    public ResponseEntity<List<ScreeningSeatView>> getSeatsForScreening(@PathVariable Long screeningId) {

        List<ScreeningSeatView> seats = queryService.getSeatsForScreening(screeningId);

        if (seats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(seats);
    }
}