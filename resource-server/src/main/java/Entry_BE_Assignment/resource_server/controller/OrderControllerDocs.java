package Entry_BE_Assignment.resource_server.controller;

import org.springframework.data.domain.Page;

import Entry_BE_Assignment.resource_server.dto.BaseApiResponse;
import Entry_BE_Assignment.resource_server.dto.OrderDto;
import Entry_BE_Assignment.resource_server.dto.OrderRequest;
import Entry_BE_Assignment.resource_server.enums.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Order", description = "주문 관련 API")
public interface OrderControllerDocs {

	@Operation(summary = "주문 생성", description = "사용자가 요청한 상품의 주문을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "주문이 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "400", description = "주문 수량이 판매 가능한 수량을 초과했습니다."),
		@ApiResponse(responseCode = "403", description = "해당 요청에 대한 권한이 없습니다."),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 상품입니다.")
	})
	BaseApiResponse
		<OrderDto> createOrder(
		OrderRequest orderRequest,
		String token);

	@Operation(summary = "주문 상태 수정", description = "특정 주문의 상태를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "주문이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "403", description = "해당 요청에 대한 권한이 없습니다."),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 상품입니다.")
	})
	public BaseApiResponse<Void> updateOrderStatus(
		Long orderId,
		OrderStatus newStatus,
		String token);

	@Operation(summary = "특정 주문 조회", description = "특정 주문의 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "주문이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "403", description = "해당 요청에 대한 권한이 없습니다."),
		@ApiResponse(responseCode = "404", description = "해당 주문을 찾을 수 없습니다.")
	})
	public BaseApiResponse<OrderDto> getOrderById(
		Long orderId,
		String token);

	@Operation(summary = "주문 리스트 조회", description = "사용자의 모든 주문 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "주문이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "403", description = "해당 요청에 대한 권한이 없습니다.")
	})
	public BaseApiResponse<Page<OrderDto>> getAllOrders(
		String token,
		int page,
		int limit
	);

	@Operation(summary = "특정 주문 취소", description = "특정 주문을 취소합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "주문이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "403", description = "해당 요청에 대한 권한이 없습니다."),
		@ApiResponse(responseCode = "404", description = "해당 주문을 찾을 수 없습니다."),
		@ApiResponse(responseCode = "409", description = "환불 절차가 필요하거나 반품/환불 처리가 필요합니다.")
	})
	public BaseApiResponse<Void> cancelOrder(
		Long orderId,
		String token);
}
