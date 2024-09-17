package Entry_BE_Assignment.resource_server.entity;

import java.math.BigDecimal;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	private String orderNumber;  // 주문 번호

	@Column(nullable = false)
	private Long buyerId;  // 구매자 ID

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();  // 주문 항목 리스트

	@Column(nullable = false)
	private int totalPrice; // 모든 상품 가격

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;  // 주문 상태

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address shippingAddress;  // 배송 주소

	public static Order createOrder(Long buyerId, String orderNumber, Address shippingAddress) {

		return Order.builder()
			.buyerId(buyerId)
			.orderNumber(orderNumber)
			.shippingAddress(shippingAddress)
			.status(OrderStatus.ORDER_PLACED)
			.build();
	}

	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.addOrder(this);
	}

	public void updateOrderStatus(OrderStatus orderStatus) {
		this.status = orderStatus;  // 기존 주문의 상태를 업데이트
	}

	public void calculateTotalPrice(List<OrderItem> orderItems) {
		// orderItems의 각 아이템에 대해 총 가격을 BigDecimal로 계산하고, 이를 합산한 후 int로 변환
		this.totalPrice = orderItems.stream()
			.map(orderItem -> new BigDecimal(orderItem.calculateTotalPrice()))  // 각 OrderItem의 총 가격을 BigDecimal로 변환
			.reduce(BigDecimal.ZERO, BigDecimal::add)  // 합산
			.intValueExact();  // 최종 값을 int로 변환하여 totalPrice에 저장
	}
}
