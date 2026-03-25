package pl.pwr.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryRequest {
    private Long orderId;
    private String customerName;
    private String courierName;
    private String deliveryStatus;
    private String productNames;
    private BigDecimal totalPrice;
}