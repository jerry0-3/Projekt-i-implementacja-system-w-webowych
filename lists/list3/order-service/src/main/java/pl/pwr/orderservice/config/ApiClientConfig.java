package pl.pwr.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.pwr.orderservice.generated.api.OrderHistoryCommandControllerApi;
import pl.pwr.orderservice.generated.invoker.ApiClient;

@Configuration
public class ApiClientConfig {

    @Bean
    public OrderHistoryCommandControllerApi orderHistoryApi() {
        // Inicjalizacja wygenerowanego klienta ze wskazaniem na adres drugiego serwisu
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://localhost:8081");

        return new OrderHistoryCommandControllerApi(apiClient);
    }
}