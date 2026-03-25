package pl.pwr.orderhistory.service;

import org.springframework.stereotype.Service;
import pl.pwr.orderhistory.model.OrderHistory;
import pl.pwr.orderhistory.repository.OrderHistoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryService(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    public OrderHistory createOrderHistory(OrderHistory orderHistory) {
        return orderHistoryRepository.save(orderHistory);
    }

    public OrderHistory updateDeliveryStatus(Long orderId, String newStatus) {
        OrderHistory history = orderHistoryRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono historii dla zamówienia o ID: " + orderId));

        history.setDeliveryStatus(newStatus);
        return orderHistoryRepository.save(history);
    }

    public Optional<OrderHistory> getOrderHistoryById(Long id) {
        return orderHistoryRepository.findById(id);
    }

    public List<OrderHistory> getAllOrderHistory() {
        return orderHistoryRepository.findAll();
    }
}