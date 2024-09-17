package Entry_BE_Assignment.resource_server.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum StatusCode {

	/**
	 * 200 번대 CODE
	 **/
	SUCCESS(HttpStatus.OK, "요청이 성공했습니다."),
	CREATED(HttpStatus.CREATED, "자원이 성공적으로 생성되었습니다."),
	ORDER_SUCCESS(HttpStatus.OK, "주문이 성공적으로 처리되었습니다."),
	ORDER_CREATED(HttpStatus.CREATED, "주문이 성공적으로 생성되었습니다."),
	ITEM_CREATED(HttpStatus.OK, "아이템이 성공적으로 생성되었습니다."),
	ITEM_SUCCESS(HttpStatus.OK, "주문이 성공적으로 처리되었습니다."),

	/**
	 * 400 번대 CODE (클라이언트 에러)
	 **/
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청 유효성 검증에 실패했습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없습니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리소스를 찾을 수 없습니다."),
	CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 자원입니다."),
	INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "유효하지 않은 주소입니다."),
	DEFAULT_ADDRESS_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "기본 주소 업데이트에 실패했습니다."),
	ORDER_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "주문 생성에 실패했습니다."),
	ORDER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 주문 요청입니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
	ORDER_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "주문 수량이 잘못되었습니다."),
	ORDER_PRICE_INVALID(HttpStatus.BAD_REQUEST, "주문 가격이 잘못되었습니다."),
	ORDER_ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "배송지를 찾을 수 없습니다."),
	ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
	ORDER_QUANTITY_EXCEEDED(HttpStatus.BAD_REQUEST, "주문 수량이 판매 가능한 수량을 초과했습니다."),

	/**
	 * 500 번대 CODE (서버 에러)
	 **/
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
	DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 처리 중 오류가 발생했습니다."),
	SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "현재 서비스 이용이 불가능합니다."),
	TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "서버 응답 시간이 초과되었습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	StatusCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	/**
	 * 이름으로 StatusCode 찾고, 없다면 defaultStatusCode 로 정의하는 함수
	 **/
	public static StatusCode findStatusCodeByNameSafe(String name, StatusCode defaultStatusCode) {
		try {
			return StatusCode.valueOf(name);
		} catch (IllegalArgumentException e) {
			return defaultStatusCode;
		}
	}
}
