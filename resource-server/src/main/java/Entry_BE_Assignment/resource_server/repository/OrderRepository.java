package Entry_BE_Assignment.resource_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Entry_BE_Assignment.resource_server.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
