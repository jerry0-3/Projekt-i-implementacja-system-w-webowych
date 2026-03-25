package pl.pwr.orderhistory.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.orderhistory.model.OrderHistory;
import pl.pwr.orderhistory.service.OrderHistoryService;

@RestController
@RequestMapping("/ordershistory")
public class OrderHistoryCommandController {

    private final OrderHistoryService service;

    public OrderHistoryCommandController(OrderHistoryService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderHistory> create(@RequestBody OrderHistory orderHistory) {
        return new ResponseEntity<>(service.createOrderHistory(orderHistory), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderHistory> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(service.updateDeliveryStatus(id, status));
    }
}