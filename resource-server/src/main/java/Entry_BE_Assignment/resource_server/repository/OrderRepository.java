package Entry_BE_Assignment.resource_server.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Entry_BE_Assignment.resource_server.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByBuyerId(Long buyerId);

	Page<Order> findByBuyerId(Long buyerId, Pageable pageable);

	@Query("SELECT o FROM Order o " +
		"JOIN o.orderItems oi " +
		"JOIN oi.item i " +
		"WHERE i.seller.id = :sellerId")
	Page<Order> findByItemSellerId(@Param("sellerId") Long sellerId, Pageable pageable);

}
