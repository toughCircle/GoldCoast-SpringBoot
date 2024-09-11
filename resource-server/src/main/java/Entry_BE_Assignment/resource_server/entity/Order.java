package Entry_BE_Assignment.resource_server.entity;

import Entry_BE_Assignment.resource_server.enums.ItemType;
import Entry_BE_Assignment.resource_server.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "orders")
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Enumerated(EnumType.STRING)
	private ItemType itemType;

	private double quantity;

	private double price;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address shippingAddress;

	public static Order createOrder(User user, ItemType itemType, double quantity, double price,
		Address shippingAddress) {
		return Order.builder()
			.user(user)
			.status(OrderStatus.ORDERED)  // 기본 주문 상태
			.itemType(itemType)
			.quantity(quantity)
			.price(price)
			.shippingAddress(shippingAddress)
			.build();
	}

}
