package pl.pwr.orderhistory.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pwr.orderhistory.model.OrderHistory;
import pl.pwr.orderhistory.repository.OrderHistoryRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderHistoryServiceTest {

    @Mock
    private OrderHistoryRepository repository;

    @InjectMocks
    private OrderHistoryService service;

    private OrderHistory mockHistory;

    @BeforeEach
    void setUp() {
        mockHistory = new OrderHistory(1L, "Jan Kowalski", "DPD", "CREATED", "Produkt A, Produkt B", new BigDecimal("150.00"));
    }

    @Test
    void shouldCreateOrderHistory() {
        // Given
        when(repository.save(any(OrderHistory.class))).thenReturn(mockHistory);

        // When
        OrderHistory result = service.createOrderHistory(mockHistory);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        verify(repository, times(1)).save(mockHistory);
    }

    @Test
    void shouldUpdateDeliveryStatus() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(mockHistory));
        when(repository.save(any(OrderHistory.class))).thenReturn(mockHistory);

        // When
        OrderHistory result = service.updateDeliveryStatus(1L, "DELIVERED");

        // Then
        assertEquals("DELIVERED", result.getDeliveryStatus());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(mockHistory);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () ->
            service.updateDeliveryStatus(99L, "DELIVERED"));

        assertTrue(exception.getMessage().contains("Nie znaleziono historii"));
        verify(repository, never()).save(any());
    }
}