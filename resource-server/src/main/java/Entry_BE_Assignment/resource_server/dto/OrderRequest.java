package Entry_BE_Assignment.resource_server.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class OrderRequest {
	@Schema(description = "주문 항목 리스트")
	private List<OrderItemRequest> orderItems;

	@Schema(description = "배송 주소")
	private AddressRequest address;
}
