package Entry_BE_Assignment.resource_server.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class OrderRequest {
	private List<OrderItemRequest> orderItems;  // 주문한 아이템 목록
	private AddressRequest address;
}
