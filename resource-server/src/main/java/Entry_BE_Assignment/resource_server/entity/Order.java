package Entry_BE_Assignment.resource_server.entity;

import java.util.ArrayList;
import java.util.List;

import Entry_BE_Assignment.resource_server.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
	private String orderNumber;

	@Column(nullable = false)
	private Long buyerId;  // 구매자 ID (인증 서버에서 받아온 ID)

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Builder.Default
	private List<OrderItem> orderItems = new ArrayList<>();  // 주문한 아이템 목록

	@Enumerated(EnumType.STRING)
	private OrderStatus status;  // 주문 상태

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address shippingAddress;  // 배송지 정보

	public static Order createOrder(Long buyerId, String orderNumber, Address shippingAddress) {
		Order order = new Order();
		order.buyerId = buyerId;
		order.orderNumber = orderNumber;
		order.shippingAddress = shippingAddress;
		order.status = OrderStatus.ORDER_PLACED;  // 초기 상태
		return order;
	}

	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.addOrder(this);
	}

	public void updateOrderStatus(OrderStatus orderStatus) {
		this.status = orderStatus;  // 기존 주문의 상태를 업데이트
	}

}
