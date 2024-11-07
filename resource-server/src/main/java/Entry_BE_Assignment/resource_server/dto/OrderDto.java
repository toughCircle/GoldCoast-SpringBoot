package Entry_BE_Assignment.resource_server.dto;

import java.time.LocalDateTime;
import java.util.List;

import Entry_BE_Assignment.resource_server.entity.Order;
import Entry_BE_Assignment.resource_server.entity.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class OrderDto {

	@Schema(description = "주문 ID", example = "123")
	Long id;

	@Schema(description = "주문 번호", example = "ORD20240913")
	String orderNumber;

	@Schema(description = "구매자 ID", example = "45")
	Long buyerId;

	@Schema(description = "주문 항목 리스트")
	List<OrderItemDto> orderItems;

	@Schema(description = "주문 상태", example = "주문 완료")
	String status;

	@Schema(description = "주문 생성일", example = "2024-09-13T14:30:00")
	LocalDateTime createdAt;

	@Schema(description = "배송 주소")
	AddressDto shippingAddress;

	public static OrderDto fromEntity(Order order) {
		return new OrderDto(
			order.getId(),
			order.getOrderNumber(),
			order.getBuyerId(),
			order.getOrderItems().stream()
				.map(OrderItemDto::fromEntity)  // OrderItem -> OrderItemDto 변환
				.toList(),
			order.getStatus().name(),
			order.getCreatedAt(),
			AddressDto.fromEntity(order.getShippingAddress())
		);
	}

	public static OrderDto fromEntityWithSellerItems(Order order, List<OrderItem> sellerOrderItems) {
		return new OrderDto(
			order.getId(),
			order.getOrderNumber(),
			order.getBuyerId(),
			sellerOrderItems.stream()
				.map(OrderItemDto::fromEntity)
				.toList(),
			order.getStatus().name(),
			order.getCreatedAt(),
			AddressDto.fromEntity(order.getShippingAddress())
		);
	}
}
