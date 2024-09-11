package Entry_BE_Assignment.resource_server.enums;

public enum OrderStatus {
	ORDERED, // 주문완료
	PAYMENT_COMPLETED, // 입금완료
	SHIPPED, // 발송완료
	IN_TRANSIT, // 배송중
	DELIVERED // 배송완료
}
