package pl.pwr.orderhistory.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.orderhistory.model.OrderHistory;
import pl.pwr.orderhistory.service.OrderHistoryService;

@RestController
@RequestMapping("/ordershistory")
@Tag(name = "CREATE")
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
        OrderHistory updatedOrderHistory = service.updateDeliveryStatus(id, status);
        return ResponseEntity.ok(updatedOrderHistory);
    }
}