package pl.pwr.orderhistory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistory {

    @Id
    private Long orderId; // Klucz główny to ID oryginalnego zamówienia

    private String customerName;

    private String courierName;

    private String deliveryStatus;

    private String productNames; // Zgodnie z poleceniem: nazwy po przecinku w jednym polu

    private BigDecimal totalPrice;
}