package Entry_BE_Assignment.resource_server.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import Entry_BE_Assignment.resource_server.entity.Item;
import Entry_BE_Assignment.resource_server.entity.Order;
import Entry_BE_Assignment.resource_server.entity.OrderItem;

@Service
public class PriceFactory {

	// 상품 수량 * 상품 가격
	public int calculateTotalPrice(Item item, BigDecimal quantity) {
		BigDecimal priceDecimal = new BigDecimal(item.getPriceGram());
		BigDecimal totalPriceDecimal = priceDecimal.multiply(quantity).setScale(0, RoundingMode.HALF_UP);

		return totalPriceDecimal.intValueExact();
	}

	public OrderItem createOrderItem(Order order, Item item, BigDecimal quantity) {
		OrderItem orderItem = OrderItem.createOrderItem(order, item, quantity);

		int totalPrice = calculateTotalPrice(item, quantity);
		orderItem.updatePrice(totalPrice);  // 가격을 엔터티에 설정

		return orderItem;
	}

}
