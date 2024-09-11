package Entry_BE_Assignment.resource_server.entity;

import Entry_BE_Assignment.resource_server.enums.ItemType;
import Entry_BE_Assignment.resource_server.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

	@Column(nullable = false, unique = true)
	private String orderNumber;  // Human Readable 형식의 주문 번호

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Column(nullable = false)
	private Long userId;  // 인증 서버에서 받아온 사용자 ID

	@Enumerated(EnumType.STRING)
	private ItemType itemType;

	private double quantity;

	private int price;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address shippingAddress;

	public static Order createOrder(ItemType itemType, double quantity, int price,
		Address shippingAddress, Long userId, String orderNumber) {
		return Order.builder()
			.status(OrderStatus.ORDER_PLACED)  // 기본 주문 상태
			.itemType(itemType)
			.quantity(quantity)
			.price(price)
			.shippingAddress(shippingAddress)
			.userId(userId)
			.orderNumber(orderNumber)
			.build();
	}

	public void updateOrderStatus(OrderStatus orderStatus) {
		this.status = orderStatus;  // 기존 주문의 상태를 업데이트
	}

}
