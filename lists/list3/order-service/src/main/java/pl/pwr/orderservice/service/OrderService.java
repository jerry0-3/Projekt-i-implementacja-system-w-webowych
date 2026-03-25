package pl.pwr.orderservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pwr.orderservice.model.DeliveryStatus;
import pl.pwr.orderservice.model.Order;
import pl.pwr.orderservice.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Order order) {
        // Ustawienie relacji dwukierunkowej, aby Hibernate poprawnie zapisał OrderItem
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setOrder(order));
        }

        // Domyślne ustawienie statusu na CREATED, jeśli dostawa istnieje
        if (order.getDelivery() != null && order.getDelivery().getStatus() == null) {
            order.getDelivery().setStatus(DeliveryStatus.CREATED);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateDeliveryStatus(Long orderId, DeliveryStatus newStatus) {
        // Znajdź zamówienie lub wyrzuć wyjątek
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia o ID: " + orderId));

        // Zaktualizuj status
        if (order.getDelivery() != null) {
            order.getDelivery().setStatus(newStatus);
        } else {
            throw new RuntimeException("Zamówienie nie ma przypisanej dostawy.");
        }

        return orderRepository.save(order);
    }
}