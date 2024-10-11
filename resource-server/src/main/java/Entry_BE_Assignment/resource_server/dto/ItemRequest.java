package Entry_BE_Assignment.resource_server.dto;

import java.math.BigDecimal;

import Entry_BE_Assignment.resource_server.enums.ItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ItemRequest {

	@Schema(description = "아이템 ID", example = "GOLD_24")
	private ItemType itemType;

	@Schema(description = "수량", example = "5.00")
	private BigDecimal quantity;
}
