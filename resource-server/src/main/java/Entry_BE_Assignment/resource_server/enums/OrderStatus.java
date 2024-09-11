package Entry_BE_Assignment.resource_server.enums;

public enum OrderStatus {
	ORDER_PLACED,  // 주문 완료
	PAYMENT_RECEIVED,  // 입금 완료 (구매)
	SHIPPED,  // 발송 완료 (구매)
	PAYMENT_SENT,  // 송금 완료 (판매)
	RECEIVED  // 수령 완료 (판매)
}
