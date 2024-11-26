package Entry_BE_Assignment.resource_server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Entry_BE_Assignment.resource_server.entity.Item;
import jakarta.persistence.LockModeType;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	// 아이템 ID로 아이템 조회
	Optional<Item> findById(Long id);

	// 판매자 ID로 아이템 조회
	List<Item> findBySellerId(Long id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT i FROM Item i WHERE i.id = :itemId")
	Optional<Item> findWithLockById(@Param("itemId") Long itemId);

}

