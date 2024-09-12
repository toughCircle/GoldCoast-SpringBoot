package Entry_BE_Assignment.resource_server.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Entry_BE_Assignment.resource_server.dto.AddressRequest;
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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;

	@Transactional
	public void createOrder(OrderRequest orderRequest, UserResponse userResponse) {

		// 구매자 권한 체크
		if (!userResponse.getRole().equals(String.valueOf(Role.BUYER))) {
			throw new BusinessException(StatusCode.FORBIDDEN);
		}

		AddressRequest addressRequest = orderRequest.getAddress();

		Address address = Address.createAddress(
			addressRequest.getZipCode(),
			addressRequest.getStreetAddress(),
			addressRequest.getAddressDetail());

		Order order = Order.createOrder(
			userResponse.getUserId(), generateUniqueOrderNumber(), address);

		orderRepository.save(order);

		for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
			Item item = itemRepository.findById(itemRequest.getItemId())
				.orElseThrow(() -> new BusinessException(StatusCode.NOT_FOUND));

			OrderItem orderItem = OrderItem.createOrderItem(order, item, itemRequest.getQuantity());
			order.addOrderItem(orderItem);
		}

	}

	// 주문 조회
	public List<Order> getOrdersByUser(Long userId) {
		return orderRepository.findByBuyerId(userId);
	}

	@Transactional
	public Order updateOrderStatus(Long orderId, OrderStatus newStatus, Role userRole) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BusinessException(StatusCode.ORDER_NOT_FOUND));

		// 구매자(BUYER) 상태 업데이트
		if (userRole == Role.BUYER) {
			if (isValidBuyerStatusUpdate(order.getStatus(), newStatus)) {
				order.updateOrderStatus(newStatus);
			} else {
				throw new BusinessException(StatusCode.FORBIDDEN);
			}
		}
		// 판매자(SELLER) 상태 업데이트
		else if (userRole == Role.SELLER) {
			if (isValidSellerStatusUpdate(order.getStatus(), newStatus)) {
				order.updateOrderStatus(newStatus);
			} else {
				throw new BusinessException(StatusCode.FORBIDDEN);
			}
		}

		return orderRepository.save(order);
	}

	// 구매자(BUYER)가 상태를 업데이트할 때 유효한 상태 전환인지 확인
	private boolean isValidBuyerStatusUpdate(OrderStatus currentStatus, OrderStatus newStatus) {
		return (currentStatus == OrderStatus.ORDER_PLACED && newStatus == OrderStatus.PAYMENT_RECEIVED)
			|| (currentStatus == OrderStatus.PAYMENT_RECEIVED && newStatus == OrderStatus.SHIPPED);
	}

	// 판매자(SELLER)가 상태를 업데이트할 때 유효한 상태 전환인지 확인
	private boolean isValidSellerStatusUpdate(OrderStatus currentStatus, OrderStatus newStatus) {
		return (currentStatus == OrderStatus.ORDER_PLACED && newStatus == OrderStatus.PAYMENT_SENT)
			|| (currentStatus == OrderStatus.PAYMENT_SENT && newStatus == OrderStatus.RECEIVED);
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
