package Entry_BE_Assignment.resource_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Entry_BE_Assignment.resource_server.entity.GoldPrice;
import Entry_BE_Assignment.resource_server.enums.ItemType;

public interface GoldPriceRepository extends JpaRepository<GoldPrice, Long> {
	Optional<GoldPrice> findByGoldType(ItemType goldType);
}
