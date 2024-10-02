package Entry_BE_Assignment.resource_server.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Entry_BE_Assignment.resource_server.dto.OrderItemRequest;
import Entry_BE_Assignment.resource_server.dto.OrderRequest;
import Entry_BE_Assignment.resource_server.entity.Address;
import Entry_BE_Assignment.resource_server.entity.Item;
import Entry_BE_Assignment.resource_server.entity.Order;
import Entry_BE_Assignment.resource_server.entity.OrderItem;
import Entry_BE_Assignment.resource_server.enums.OrderStatus;
import Entry_BE_Assignment.resource_server.enums.Role;
import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.exception.customException.BusinessException;
import Entry_BE_Assignment.resource_server.grpc.UserResponse;
import Entry_BE_Assignment.resource_server.repository.ItemRepository;
import Entry_BE_Assignment.resource_server.repository.OrderRepository;
import Entry_BE_Assignment.resource_server.validation.OrderValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;
	private final OrderValidator orderValidator;
	private final PriceFactory priceFactory;

	@Transactional
	public void createOrder(OrderRequest orderRequest, UserResponse userResponse) {

		// 구매자 권한 체크
		orderValidator.validateUserRole(userResponse.getRole(), Role.BUYER);

		// 주소 생성
		Address address = Address.createAddress(
			orderRequest.getAddress().getZipCode(),
			orderRequest.getAddress().getStreetAddress(),
			orderRequest.getAddress().getAddressDetail());

		// 주문 생성
		Order order = Order.createOrder(userResponse.getUserId(), generateUniqueOrderNumber(), address);
		orderRepository.save(order);

		for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
			Item item = itemRepository.findById(itemRequest.getItemId())
				.orElseThrow(() -> new BusinessException(StatusCode.ITEM_NOT_FOUND));

			orderValidator.validateItemQuantity(itemRequest.getQuantity(), item.getQuantity());

			OrderItem orderItem = priceFactory.createOrderItem(order, item, itemRequest.getQuantity());
			order.addOrderItem(orderItem);

			// 각 상품의 판매 가능 수량 조정
			item.updateItemQuantity(item.getQuantity().subtract(itemRequest.getQuantity()));
		}

		int totalPrice = calculateTotalPrice(order.getOrderItems());
		order.updateTotalPrice(totalPrice);

		orderRepository.save(order);
	}

	// 주문 총 금액 계산
	private int calculateTotalPrice(List<OrderItem> orderItems) {
		return orderItems.stream()
			.map(orderItem -> new BigDecimal(
				priceFactory.calculateTotalPrice(orderItem.getItem(), orderItem.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.intValueExact();
	}

	// 주문 조회
	public List<Order> getOrdersByUser(Long userId) {
		return orderRepository.findByBuyerId(userId);
	}

	@Transactional
	public Order updateOrderStatus(Long orderId, OrderStatus newStatus, Role userRole) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BusinessException(StatusCode.ORDER_NOT_FOUND));

		// 동일한 상태로의 전환을 방지
		if (order.getStatus() == newStatus) {
			throw new BusinessException(StatusCode.INVALID_STATUS_UPDATE);
		}

		// 상태 전환 유효성 검증
		orderValidator.validateOrderStatusTransition(order.getStatus(), newStatus, userRole);

		// 상태 업데이트
		order.updateOrderStatus(newStatus);

		return order;
	}

	private String generateUniqueOrderNumber() {
		String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String randomPart = String.format("%04d", (int)(Math.random() * 10000));
		return "ORD-" + datePart + "-" + randomPart;
	}

	public Order getOrderById(Long orderId, UserResponse userResponse) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BusinessException(StatusCode.ORDER_NOT_FOUND));

		String userId = String.valueOf(userResponse.getUserId());

		// 1. 만약 주문을 생성한 본인(구매자)이라면 접근 허용
		if (userId.equals(String.valueOf(order.getBuyerId()))) {
			return order;
		}

		// 2. 주문에 포함된 아이템 중 하나라도 해당 사용자가 판매자인 경우 접근 허용
		boolean isSellerOfAnyItem = order.getOrderItems().stream()
			.anyMatch(orderItem -> orderItem.getItem().getSellerId().equals(Long.parseLong(userId)));

		if (isSellerOfAnyItem) {
			return order;
		}

		// 3. 위 조건을 만족하지 않으면 접근 불가 예외 처리
		throw new BusinessException(StatusCode.FORBIDDEN);

	}

	// 전체 주문 목록 조회 (구매자와 판매자)
	public List<Order> getAllOrders(UserResponse userResponse) {
		String userId = String.valueOf(userResponse.getUserId());

		// 1. 구매자는 자신이 생성한 주문 목록 조회
		if (userResponse.getRole().equals(String.valueOf(Role.BUYER))) {
			return orderRepository.findByBuyerId(Long.parseLong(userId));
		}

		// 2. 판매자는 자신이 등록한 아이템이 포함된 주문 목록 조회
		if (userResponse.getRole().equals(String.valueOf(Role.SELLER))) {
			return orderRepository.findBySellerId(Long.parseLong(userId));
		}

		// 3. 그 외의 경우는 접근 불가
		throw new BusinessException(StatusCode.FORBIDDEN);
	}

	// 주문 삭제 (구매자만 가능)
	@Transactional
	public void deleteOrder(Long orderId, UserResponse userResponse) {
		if (!userResponse.getRole().equals(String.valueOf(Role.BUYER))) {
			throw new BusinessException(StatusCode.FORBIDDEN);
		}

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BusinessException(StatusCode.ORDER_NOT_FOUND));

		orderRepository.delete(order);
	}
}
