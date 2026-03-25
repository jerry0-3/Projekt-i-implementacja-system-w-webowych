package pl.pwr.orderhistory.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import pl.pwr.orderhistory.model.OrderHistory;
import pl.pwr.orderhistory.service.OrderHistoryService;

import java.util.List;

@RestController
@RequestMapping("/ordershistory")
public class OrderHistoryQueryController {

    private final OrderHistoryService service;

    public OrderHistoryQueryController(OrderHistoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderHistory>> getAll() {
        return ResponseEntity.ok(service.getAllOrderHistory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderHistory> getById(@PathVariable Long id) {
        return service.getOrderHistoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paged")
    public ResponseEntity<PagedModel<EntityModel<OrderHistory>>> getPaged(
            Pageable pageable,
            PagedResourcesAssembler<OrderHistory> assembler) {

        Page<OrderHistory> page = service.getOrderHistoryPaged(pageable);

        // Assembler automatycznie generuje model HAL-JSON z linkami nawigacyjnymi
        PagedModel<EntityModel<OrderHistory>> pagedModel = assembler.toModel(page);

        return ResponseEntity.ok(pagedModel);
    }
}