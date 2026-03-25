package pl.pwr.orderhistory.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
}