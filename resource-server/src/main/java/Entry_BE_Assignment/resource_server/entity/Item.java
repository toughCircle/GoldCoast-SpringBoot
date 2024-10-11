package Entry_BE_Assignment.resource_server.entity;

import java.math.BigDecimal;

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
	private ItemType itemType;

	@Column(nullable = false)
	private int priceGram;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal quantity;

	private Long sellerId;

	public static Item createItem(ItemType itemType, int price, BigDecimal quantity, Long sellerId) {
		return Item.builder()
			.itemType(itemType)
			.priceGram(price)
			.quantity(quantity)
			.sellerId(sellerId)
			.build();
	}

	public void updateItem(ItemType itemType, BigDecimal quantity) {
		this.itemType = itemType;
		this.quantity = quantity;
	}

	public void updateItemQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

}
