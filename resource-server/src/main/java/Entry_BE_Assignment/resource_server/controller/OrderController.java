package Entry_BE_Assignment.resource_server.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Entry_BE_Assignment.resource_server.dto.BaseApiResponse;
import Entry_BE_Assignment.resource_server.dto.OrderDto;
import Entry_BE_Assignment.resource_server.dto.OrderRequest;
import Entry_BE_Assignment.resource_server.entity.Order;
import Entry_BE_Assignment.resource_server.enums.OrderStatus;
import Entry_BE_Assignment.resource_server.enums.Role;
import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.grpc.AuthServiceGrpc;
import Entry_BE_Assignment.resource_server.grpc.TokenRequest;
import Entry_BE_Assignment.resource_server.grpc.UserResponse;
import Entry_BE_Assignment.resource_server.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController implements OrderControllerDocs {

	private final OrderService orderService;
	private final AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;  // gRPC 클라이언트

	@PostMapping
	public BaseApiResponse<OrderDto> createOrder(
		@RequestBody OrderRequest orderRequest,
		@RequestHeader("Authorization") String token) {

		UserResponse userResponse = getUserResponse(token);

		OrderDto orderDto = orderService.createOrder(orderRequest, userResponse);
		return BaseApiResponse.of(StatusCode.ORDER_CREATED, orderDto);
	}

	// 주문 상태 업데이트 (역할에 따라 처리)
	@PatchMapping("/{orderId}/status")
	public BaseApiResponse<Void> updateOrderStatus(
		@PathVariable Long orderId,
		@RequestParam OrderStatus newStatus,
		@RequestHeader("Authorization") String token) {

		UserResponse userResponse = getUserResponse(token);

		Order updatedOrder = orderService.updateOrderStatus(
			orderId, newStatus, Role.valueOf(userResponse.getRole()));

		return BaseApiResponse.of(StatusCode.ORDER_SUCCESS);
	}

	// 주문 조회 (ID로 조회)
	@GetMapping("/{orderId}")
	public BaseApiResponse<OrderDto> getOrderById(
		@PathVariable Long orderId,
		@RequestHeader("Authorization") String token) {

		UserResponse userResponse = getUserResponse(token);

		OrderDto order = orderService.getOrderById(orderId, userResponse);
		return BaseApiResponse.of(StatusCode.ORDER_SUCCESS, order);
	}

	// 전체 주문 목록 조회
	@GetMapping
	public BaseApiResponse<Page<OrderDto>> getAllOrders(
		@RequestHeader("Authorization") String token,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "5") int limit
	) {

		UserResponse userResponse = getUserResponse(token);

		PageRequest pageRequest = PageRequest.of(page - 1, limit); // 페이지는 0부터 시작하므로 -1

		Page<OrderDto> orders = orderService.getAllOrders(userResponse, pageRequest);
		return BaseApiResponse.of(StatusCode.ORDER_SUCCESS, orders);
	}

	// 주문 취소
	@PatchMapping("/{orderId}/cancel")
	public BaseApiResponse<Void> cancelOrder(@PathVariable Long orderId,
		@RequestHeader("Authorization") String token) {

		UserResponse userResponse = getUserResponse(token);

		orderService.cancelOrder(orderId, userResponse);
		return BaseApiResponse.of(StatusCode.ORDER_SUCCESS);
	}

	private UserResponse getUserResponse(String token) {
		// JWT 토큰에서 "Bearer " 제거
		String jwtToken = token.substring(7);

		// gRPC로 인증 서버에 사용자 정보 요청
		TokenRequest tokenRequest = TokenRequest.newBuilder().setToken(jwtToken).build();
		return authServiceStub.validateToken(tokenRequest);
	}

}
