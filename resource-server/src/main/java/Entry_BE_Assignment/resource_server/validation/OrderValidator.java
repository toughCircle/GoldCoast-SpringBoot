package Entry_BE_Assignment.resource_server.validation;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import Entry_BE_Assignment.resource_server.enums.OrderStatus;
import Entry_BE_Assignment.resource_server.enums.Role;
import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.exception.customException.BusinessException;
import Entry_BE_Assignment.resource_server.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidator {

	private final ItemRepository itemRepository;

	// 주문 상태 전환 검증
	public void validateOrderStatusTransition(OrderStatus currentStatus, OrderStatus newStatus, Role role) {
		if (role == Role.BUYER) {
			if (!isValidBuyerStatusUpdate(currentStatus, newStatus)) {
				throw new BusinessException(StatusCode.FORBIDDEN);
			}
		} else if (role == Role.SELLER) {
			if (!isValidSellerStatusUpdate(currentStatus, newStatus)) {
				throw new BusinessException(StatusCode.FORBIDDEN);
			}
		}
	}

	// 구매자의 상태 전환 유효성 확인
	private boolean isValidBuyerStatusUpdate(OrderStatus currentStatus, OrderStatus newStatus) {
		return (currentStatus == OrderStatus.ORDER_PLACED && newStatus == OrderStatus.PAYMENT_RECEIVED)
			|| (currentStatus == OrderStatus.PAYMENT_RECEIVED && newStatus == OrderStatus.SHIPPED);
	}

	// 판매자의 상태 전환 유효성 확인
	private boolean isValidSellerStatusUpdate(OrderStatus currentStatus, OrderStatus newStatus) {
		return (currentStatus == OrderStatus.ORDER_PLACED)
			|| (newStatus == OrderStatus.RECEIVED);
	}

	// 구매자/판매자 권한 검증
	public void validateUserRole(String userRole, Role expectedRole) {
		Role actualRole = Role.valueOf(userRole.toUpperCase());  // String을 Role로 변환
		if (actualRole != expectedRole) {
			throw new BusinessException(StatusCode.FORBIDDEN);
		}
	}

	// 구매가능 수량 검증
	public void validateItemQuantity(BigDecimal requestedQuantity, BigDecimal availableQuantity) {
		if (requestedQuantity.compareTo(availableQuantity) > 0) {
			throw new BusinessException(StatusCode.ORDER_QUANTITY_EXCEEDED);
		}
	}
}
