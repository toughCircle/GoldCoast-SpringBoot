package Entry_BE_Assignment.resource_server.enums;

public enum OrderStatus {
	ORDER_PLACED,  // 주문 완료
	ORDER_CANCELLED, // 주문 취소
	REFUND_REQUESTED, // 환불 요청 (구매)
	REFUND_COMPLETED, // 환불 완료 (판매)
	RETURN_REQUESTED, // 반품 요청 (구매)
	RETURN_COMPLETED, // 반품 완료 (판매)
	PAYMENT_RECEIVED,  // 입금 완료 (구매)
	SHIPPED,  // 발송 완료 (구매)
	RECEIVED  // 수령 완료 (판매)
}
