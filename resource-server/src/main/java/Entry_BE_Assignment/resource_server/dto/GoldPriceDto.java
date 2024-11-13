package Entry_BE_Assignment.resource_server.dto;

import Entry_BE_Assignment.resource_server.entity.GoldPrice;
import Entry_BE_Assignment.resource_server.enums.ItemType;

public record GoldPriceDto(Long id, ItemType goldType, int price) {

	public static GoldPriceDto fromEntity(GoldPrice goldPrice) {
		return new GoldPriceDto(
			goldPrice.getId(),
			goldPrice.getGoldType(),
			goldPrice.getPrice()
		);
	}

}
