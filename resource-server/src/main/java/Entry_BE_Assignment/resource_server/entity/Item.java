package Entry_BE_Assignment.resource_server.entity;

import Entry_BE_Assignment.resource_server.enums.ItemType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "items")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private ItemType itemType;  // 품목 (99.9% 금, 99.99% 금)

	@Column(nullable = false)
	private int price;  // 상품 가격

	private Long sellerId;

	public static Item createItem(ItemType itemType, int price, Long sellerId) {
		Item item = new Item();
		item.itemType = itemType;
		item.price = price;
		item.sellerId = sellerId;
		return item;
	}

	public void updateItem(ItemType itemType, int price) {
		this.itemType = itemType;
		this.price = price;
	}

}
