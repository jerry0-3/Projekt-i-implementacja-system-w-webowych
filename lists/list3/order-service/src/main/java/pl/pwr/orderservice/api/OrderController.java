package pl.pwr.orderservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.orderservice.model.DeliveryStatus;
import pl.pwr.orderservice.model.Order;
import pl.pwr.orderservice.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/delivery-status")
    public ResponseEntity<Order> updateDeliveryStatus(@PathVariable Long id, @RequestParam DeliveryStatus status) {
        Order updatedOrder = orderService.updateDeliveryStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
}