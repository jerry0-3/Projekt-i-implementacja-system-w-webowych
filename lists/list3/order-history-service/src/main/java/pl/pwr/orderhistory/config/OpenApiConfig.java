package pl.pwr.orderhistory.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // Definicja grupy "orders-history" dla odczytu (widoczna na Rysunku 4)
    @Bean
    public GroupedOpenApi historyApi() {
        return GroupedOpenApi.builder()
                .group("ordershistory")
                .pathsToMatch("/ordershistory", "/ordershistory/{id}")
                .build();
    }

    // Definicja grupy "sync" dla zapisu i aktualizacji (widoczna na Rysunku 5)
    @Bean
    public GroupedOpenApi syncApi() {
        return GroupedOpenApi.builder()
                .group("sync")
                .pathsToMatch("/ordershistory/create", "/ordershistory/{id}/status")
                .build();
    }
}