package pl.pwr.orderservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pwr.orderservice.client.OrderHistoryClient;
import pl.pwr.orderservice.model.DeliveryStatus;
import pl.pwr.orderservice.model.Order;
import pl.pwr.orderservice.repository.OrderRepository;
import pl.pwr.orderservice.generated.api.OrderHistoryCommandControllerApi;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderHistoryClient orderHistoryClient;
    // private final OrderHistoryCommandControllerApi historyApi; // WYGENEROWANY KLIENT!

    public OrderService(OrderRepository orderRepository, OrderHistoryClient orderHistoryClient) {
        this.orderRepository = orderRepository;
        this.orderHistoryClient = orderHistoryClient;
    }
//    public OrderService(OrderRepository orderRepository, OrderHistoryCommandControllerApi historyApi) {
//        this.orderRepository = orderRepository;
//        this.historyApi = historyApi;
//    }

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

        Order savedOrder = orderRepository.save(order);

        // Synchronizacja: wysłanie danych do drugiego mikroserwisu
        orderHistoryClient.syncNewOrder(savedOrder);

        // Transformacja do wygenerowanego modelu
//        pl.pwr.orderservice.generated.model.OrderHistory generatedHistory = new pl.pwr.orderservice.generated.model.OrderHistory();
//        generatedHistory.setOrderId(savedOrder.getId());
//        generatedHistory.setCustomerName(savedOrder.getCustomerName());
//        generatedHistory.setCourierName(savedOrder.getDelivery() != null ? savedOrder.getDelivery().getCourierName() : "Brak");
//        generatedHistory.setDeliveryStatus(savedOrder.getDelivery() != null ? savedOrder.getDelivery().getStatus().name() : "CREATED");
//
//        String products = savedOrder.getItems().stream()
//                .map(item -> item.getProduct().getName())
//                .collect(Collectors.joining(", "));
//        generatedHistory.setProductNames(products);
//
//        BigDecimal total = savedOrder.getItems().stream()
//                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        generatedHistory.setTotalPrice(total);
//
//        // Wywołanie API za pomocą wygenerowanego klienta
//        historyApi.create(generatedHistory);

        return savedOrder;
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

        Order updatedOrder = orderRepository.save(order);

        // Synchronizacja: wysłanie zmiany statusu do drugiego mikroserwisu
        orderHistoryClient.syncDeliveryStatus(orderId, newStatus);

        // Wywołanie zmiany statusu za pomocą wygenerowanego klienta
        // historyApi.updateStatus(orderId, newStatus.name());

        return updatedOrder;
    }
}