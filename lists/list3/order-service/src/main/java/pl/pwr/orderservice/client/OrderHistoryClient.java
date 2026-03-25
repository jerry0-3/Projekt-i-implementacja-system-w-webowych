package pl.pwr.orderservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.pwr.orderservice.dto.OrderHistoryRequest;
import pl.pwr.orderservice.model.DeliveryStatus;
import pl.pwr.orderservice.model.Order;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class OrderHistoryClient {

    private final RestTemplate restTemplate;
    // Adres serwisu z historią zamówień
    private final String historyServiceUrl = "http://localhost:8081/ordershistory";

    public OrderHistoryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void syncNewOrder(Order order) {
        // Zgodnie z wymaganiem: nazwy po przecinku
        String productNames = order.getItems().stream()
                .map(item -> item.getProduct().getName())
                .collect(Collectors.joining(", "));

        // Zgodnie z wymaganiem: sumaryczna wartość
        BigDecimal totalPrice = order.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String courier = order.getDelivery() != null ? order.getDelivery().getCourierName() : "Brak";
        String status = order.getDelivery() != null ? order.getDelivery().getStatus().name() : DeliveryStatus.CREATED.name();

        OrderHistoryRequest request = new OrderHistoryRequest(
                order.getId(),
                order.getCustomerName(),
                courier,
                status,
                productNames,
                totalPrice
        );

        // Wywołanie POST na Order History Service
        restTemplate.postForObject(historyServiceUrl + "/create", request, Void.class);
    }

    public void syncDeliveryStatus(Long orderId, DeliveryStatus newStatus) {
        String url = historyServiceUrl + "/" + orderId + "/status?status=" + newStatus.name();
        // Wywołanie PATCH na Order History Service
        restTemplate.patchForObject(url, null, Void.class);
    }
}