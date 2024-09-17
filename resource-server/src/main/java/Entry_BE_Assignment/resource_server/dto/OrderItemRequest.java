package Entry_BE_Assignment.resource_server.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;

@Getter
public class OrderItemRequest {

	@Schema(description = "아이템 ID", example = "101")
	private Long itemId;

	@Schema(description = "수량", example = "2.5")
	@DecimalMin(value = "0.01", message = "수량은 0.01 이상이어야 합니다.")
	@Digits(integer = 5, fraction = 2, message = "최대 소수점 2자리까지 입력 가능합니다.")
	private BigDecimal quantity;
}
