package Entry_BE_Assignment.resource_server.dto;

import java.math.BigDecimal;

import Entry_BE_Assignment.resource_server.entity.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class ItemDto {

	@Schema(description = "아이템 ID", example = "101")
	Long id;

	@Schema(description = "아이템 유형", example = "99.9% 금")
	String itemType;

	@Schema(description = "가격", example = "100000")
	int price;

	@Schema(description = "재고", example = "10.5")
	BigDecimal quantity;

	@Schema(description = "판매자 ID", example = "10")
	Long sellerId;

	public static ItemDto fromEntity(Item item) {
		return new ItemDto(
			item.getId(),
			item.getItemType().name(),
			item.getPriceGram(),
			item.getQuantity(),
			item.getSellerId()
		);
	}

}
