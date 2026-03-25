package pl.pwr.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pwr.orderservice.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Spring Data JPA automatycznie dostarcza metody takie jak save(), findById() itp.
}