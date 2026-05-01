package com.example.cinemacommandservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                // Wyłączamy CSRF (niezbędne dla REST API i konsoli H2)
                .csrf(AbstractHttpConfigurer::disable)
                // Pozwala na działanie konsoli H2 w ramkach (iframe)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        // 1. PUBLICZNE: Swagger UI oraz dokumentacja OpenAPI
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // 2. PUBLICZNE: Konsola bazy danych H2
                        .requestMatchers("/h2-console/**").permitAll()
                        // 3. PUBLICZNE: Widz może kupić bilet bez logowania
                        .requestMatchers(HttpMethod.POST, "/api/commands/tickets").permitAll()

                        // 4. CHRONIONE: Tylko Bileter może skanować i skasować bilet
                        .requestMatchers(HttpMethod.GET, "/api/commands/tickets/*").hasRole("INSPECTOR")
                        .requestMatchers(HttpMethod.POST, "/api/commands/tickets/*/consume").hasRole("INSPECTOR")
                        .requestMatchers(HttpMethod.GET, "/api/commands/tickets/verify").hasRole("INSPECTOR")

                        // Zabezpieczenie na wypadek dodania nowych endpointów
                        .anyRequest().authenticated()
                )
                // Używamy prostego logowania Basic Auth
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Tworzymy testowego biletera zapisanego w pamięci aplikacji
        UserDetails bileter = User.builder()
                .username("bileter")
                .password(passwordEncoder().encode("haslo123"))
                .roles("INSPECTOR")
                .build();

        return new InMemoryUserDetailsManager(bileter);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Wymagane przez Spring Security do bezpiecznego hashowania haseł
        return new BCryptPasswordEncoder();
    }
}