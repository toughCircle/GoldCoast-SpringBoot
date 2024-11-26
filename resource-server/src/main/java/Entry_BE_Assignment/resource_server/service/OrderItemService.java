package Entry_BE_Assignment.resource_server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import Entry_BE_Assignment.resource_server.dto.OrderItemRequest;
import Entry_BE_Assignment.resource_server.entity.Item;
import Entry_BE_Assignment.resource_server.entity.Order;
import Entry_BE_Assignment.resource_server.entity.OrderItem;
import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.exception.customException.BusinessException;
import Entry_BE_Assignment.resource_server.repository.ItemRepository;
import Entry_BE_Assignment.resource_server.validation.OrderValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {

	private final ItemRepository itemRepository;
	private final OrderValidator orderValidator;
	private final PriceFactory priceFactory;

	@Transactional
	public List<OrderItem> createOrderItems(Order order, List<OrderItemRequest> orderItemRequests) {
		List<OrderItem> orderItems = new ArrayList<>();

		for (OrderItemRequest itemRequest : orderItemRequests) {
			// 상품 존재 여부 확인
			Item item = itemRepository.findWithLockById(itemRequest.getItemId())
				.orElseThrow(() -> new BusinessException(StatusCode.ITEM_NOT_FOUND));

			// 상품 수량 검증
			orderValidator.validateItemQuantity(itemRequest.getQuantity(), item.getQuantity());

			// 주문 아이템 생성 및 판매 가능 수량 감소
			OrderItem orderItem = priceFactory.createOrderItem(order, item, itemRequest.getQuantity());
			orderItems.add(orderItem);
			item.updateItemQuantity(item.getQuantity().subtract(itemRequest.getQuantity()));
		}

		return orderItems;
	}

}
