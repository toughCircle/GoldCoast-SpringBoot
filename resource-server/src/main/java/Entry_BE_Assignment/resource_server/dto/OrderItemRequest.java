package Entry_BE_Assignment.resource_server.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;

@Getter
public class OrderItemRequest {
	private Long itemId;  // 아이템 ID

	@DecimalMin(value = "0.01", message = "수량은 0.01 이상이어야 합니다.")
	@Digits(integer = 5, fraction = 2, message = "최대 소수점 2자리까지 입력 가능합니다.")
	private BigDecimal quantity;  // 수량
}
