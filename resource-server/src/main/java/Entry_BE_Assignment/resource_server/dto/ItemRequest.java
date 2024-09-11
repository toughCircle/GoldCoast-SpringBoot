package Entry_BE_Assignment.resource_server.dto;

import Entry_BE_Assignment.resource_server.enums.ItemType;
import lombok.Getter;

@Getter
public class ItemRequest {
	private ItemType itemType;  // 품목 (99.9% 금, 99.99% 금)
	private int price;   // 가격
}
