package Entry_BE_Assignment.resource_server.entity;

import Entry_BE_Assignment.resource_server.enums.ItemType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "gold_price")
public class GoldPrice extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "gold_type", nullable = false)
	private ItemType goldType;

	@Column(name = "price", nullable = false)
	private int price;  // 금 시세

	public static GoldPrice createGoldPrice(ItemType goldType, int price) {
		return GoldPrice.builder()
			.goldType(goldType)
			.price(price)
			.build();
	}

	public void updatePrice(int price) {
		this.price = price;
	}

}
