package pl.pwr.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Używamy HttpComponents, by bez problemu obsługiwać metodę PATCH
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}