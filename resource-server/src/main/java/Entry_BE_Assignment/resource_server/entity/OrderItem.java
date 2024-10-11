package Entry_BE_Assignment.resource_server.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "order_item")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;  // 주문 정보

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;  // 아이템 정보

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal quantity;  // 주문한 해당 아이템의 수량

	@Column(nullable = false)
	private int price;  // 해당 아이템의 총 가격 (수량 * 아이템 가격)

	public static OrderItem createOrderItem(Order order, Item item, BigDecimal quantity) {
		return OrderItem.builder()
			.order(order)
			.item(item)
			.quantity(quantity)
			.build();
	}

	public void addOrder(Order order) {
		this.order = order;
	}

	public void updatePrice(int price) {
		this.price = price;
	}

}