package Entry_BE_Assignment.resource_server.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Entry_BE_Assignment.resource_server.dto.OrderDto;
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
	private final AddressService addressService;
	private final ItemRepository itemRepository;
	private final OrderItemService orderItemService;
	private final OrderValidator orderValidator;
	private final PriceFactory priceFactory;

	private static final String ORDER_PREFIX = "ORD-";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
	private static final int RANDOM_PART_LENGTH = 4;
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	@Transactional
	public OrderDto createOrder(OrderRequest orderRequest, UserResponse userResponse) {

		// 구매자 권한 체크
		orderValidator.validateUserRole(userResponse.getRole(), Role.BUYER);

		// 주소 처리 (중복 확인 및 생성)
		Address address = addressService.getOrCreateAddress(orderRequest.getAddress());

		// 주문 생성
		Order order = Order.createOrder(userResponse.getUserId(), generateUniqueOrderNumber(), address);

		// 주문 아이템 추가
		List<OrderItem> orderItems = orderItemService.createOrderItems(order, orderRequest.getOrderItems());
		order.addOrderItems(orderItems);

		// 총 가격 계산 및 업데이트
		int totalPrice = priceFactory.calculateTotalPrice(orderItems);
		order.updateTotalPrice(totalPrice);

		// 주문 저장
		Order savedOrder = orderRepository.save(order);

		return OrderDto.fromEntity(savedOrder);
	}

	// 주문 조회
	public List<Order> getOrdersByUser(Long userId) {
		return orderRepository.findByBuyerId(userId);
	}

	@Transactional
	public Order updateOrderStatus(Long orderId, OrderStatus newStatus, Role userRole) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BusinessException(StatusCode.ORDER_NOT_FOUND));

		if (order.getStatus() == newStatus) {
			throw new BusinessException(StatusCode.INVALID_STATUS_UPDATE);
		}

		orderValidator.validateOrderStatusTransition(order.getStatus(), newStatus, userRole);

		order.updateOrderStatus(newStatus);

		return order;
	}

	private String generateUniqueOrderNumber() {
		String datePart = LocalDateTime.now().format(DATE_FORMATTER);
		long currentMillis = System.currentTimeMillis();
		int randomValue = SECURE_RANDOM.nextInt((int)Math.pow(10, RANDOM_PART_LENGTH));
		long combinedValue = currentMillis + randomValue;

		return ORDER_PREFIX + datePart + "-" + combinedValue;
	}

	public OrderDto getOrderById(Long orderId, UserResponse userResponse) {
		Order order = findOrderById(orderId);
		String userId = String.valueOf(userResponse.getUserId());

		if (userId.equals(String.valueOf(order.getBuyerId()))) {
			return OrderDto.fromEntity(order);
		}

		List<OrderItem> sellerOrderItems = order.getOrderItems().stream()
			.filter(orderItem -> orderItem.getItem().getSellerId().equals(Long.parseLong(userId)))
			.collect(Collectors.toList());

		if (!sellerOrderItems.isEmpty()) {
			return OrderDto.fromEntityWithSellerItems(order, sellerOrderItems);
		}

		throw new BusinessException(StatusCode.FORBIDDEN);

	}

	// 전체 주문 목록 조회 (구매자와 판매자)
	public Page<OrderDto> getAllOrders(UserResponse userResponse, Pageable pageable) {
		Long userId = userResponse.getUserId();

		if (userResponse.getRole().equals(String.valueOf(Role.BUYER))) {
			// 구매자의 경우
			Page<Order> byBuyerId = orderRepository.findByBuyerId(userId, pageable);
			return byBuyerId.map(OrderDto::fromEntity); // 기존 변환 로직 유지
		}

		if (userResponse.getRole().equals(String.valueOf(Role.SELLER))) {
			// 판매자의 경우
			Page<Order> bySellerId = orderRepository.findByItemSellerId(userId, pageable);
			return bySellerId.map(order -> {
				// 특정 판매자와 관련된 OrderItems만 필터링
				List<OrderItem> sellerOrderItems = order.getOrderItems().stream()
					.filter(orderItem -> orderItem.getItem().getSellerId().equals(userId))
					.toList();

				// fromEntityWithSellerItems 메서드 사용하여 변환
				return OrderDto.fromEntityWithSellerItems(order, sellerOrderItems);
			});
		}

		throw new BusinessException(StatusCode.FORBIDDEN);
	}

	// 주문 취소 (구매자만 가능)
	@Transactional
	public void cancelOrder(Long orderId, UserResponse userResponse) {
		if (!userResponse.getRole().equals(String.valueOf(Role.BUYER))) {
			throw new BusinessException(StatusCode.FORBIDDEN);
		}

		Order order = findOrderById(orderId);

		// 입금 전이라면 취소 가능
		if (order.getStatus() == OrderStatus.ORDER_PLACED) {
			order.updateOrderStatus(OrderStatus.ORDER_CANCELLED);

			// 재고 복구 로직: 각 주문 항목의 수량을 원래 상태로 되돌림
			for (OrderItem orderItem : order.getOrderItems()) {
				Item item = orderItem.getItem();
				item.increaseStock(orderItem.getQuantity());
			}

		} else if (order.getStatus() == OrderStatus.PAYMENT_RECEIVED || order.getStatus() == OrderStatus.SHIPPED) {
			// 입금 후부터는 환불 절차로 처리
			throw new BusinessException(StatusCode.REFUND_REQUIRED);
		} else {
			// 발송 완료 이후라면 반품 및 환불 절차 진행
			throw new BusinessException(StatusCode.RETURN_OR_REFUND_REQUIRED);
		}

	}

	private Order findOrderById(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new BusinessException(StatusCode.ORDER_NOT_FOUND));
	}

	public Page<OrderDto> getOrdersByItemId(Long itemId, UserResponse userResponse, Pageable pageable) {

		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(StatusCode.ITEM_NOT_FOUND));

		Page<Order> orders;
		if (userResponse.getUserId() == item.getSellerId()) {
			orders = orderRepository.findByItemId(itemId, pageable);
		} else {
			throw new BusinessException(StatusCode.FORBIDDEN);
		}

		// Order 엔터티 리스트를 OrderDto 리스트로 변환
		return orders.map(OrderDto::fromEntity);
	}

}
