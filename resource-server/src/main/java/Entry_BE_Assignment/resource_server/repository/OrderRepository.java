package Entry_BE_Assignment.resource_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Entry_BE_Assignment.resource_server.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserId(Long userId);

	@Query("SELECT o FROM Order o JOIN o.orderItems oi WHERE oi.item.sellerId = :sellerId")
	List<Order> findBySellerId(@Param("sellerId") Long sellerId);

}
