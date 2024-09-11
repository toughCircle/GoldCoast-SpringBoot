package Entry_BE_Assignment.resource_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Entry_BE_Assignment.resource_server.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	// 아이템 ID로 아이템 조회
	Optional<Item> findById(Long id);
}

