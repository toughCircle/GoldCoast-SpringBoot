package Entry_BE_Assignment.resource_server.dto;

import java.math.BigDecimal;

import Entry_BE_Assignment.resource_server.entity.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class OrderItemDto {

	@Schema(description = "주문 항목 ID", example = "1")
	Long id;

	@Schema(description = "아이템 정보")
	ItemDto item;

	@Schema(description = "주문 수량", example = "2.5")
	BigDecimal quantity;

	@Schema(description = "총 가격", example = "250000")
	int totalPrice;

	public static OrderItemDto fromEntity(OrderItem orderItem) {
		return new OrderItemDto(
			orderItem.getId(),
			ItemDto.fromEntity(orderItem.getItem()),  // Item 엔터티에서 변환
			orderItem.getQuantity(),
			orderItem.getPrice()
		);
	}
}
