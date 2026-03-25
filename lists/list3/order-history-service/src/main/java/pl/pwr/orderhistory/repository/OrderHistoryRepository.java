package pl.pwr.orderhistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pwr.orderhistory.model.OrderHistory;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    // Spring Data JPA automatycznie dostarcza metody takie jak save(), findById() itp.
}