package Entry_BE_Assignment.auth_server.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum StatusCode {

	/**
	 * 200 번대 CODE
	 **/
	OK(HttpStatus.OK, "요청이 성공했습니다."),
	CREATED(HttpStatus.CREATED, "생성되었습니다."),
	LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
	SIGN_UP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료되었습니다."),
	TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "토큰이 재발급되었습니다."),

	/**
	 * 400 번대 CODE
	 **/
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 정보입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다."),
	EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 만료되었습니다."),
	EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
	DUPLICATE_ACCOUNT(HttpStatus.CONFLICT, "이미 사용 중인 계정입니다."),
	PASSWORD_TOO_WEAK(HttpStatus.BAD_REQUEST, "비밀번호는 최소 10자 이상이어야 하며, 강력한 비밀번호가 필요합니다."),
	PASSWORD_TOO_SIMPLE(HttpStatus.BAD_REQUEST, "비밀번호는 숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다."),
	PASSWORD_REPEATING_CHARACTER(HttpStatus.BAD_REQUEST, "비밀번호에 연속된 문자를 사용할 수 없습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원되지 않는 HTTP 메서드입니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "계정이 잠겼습니다. 관리자에게 문의하세요."),

	/**
	 * 500 번대 CODE
	 **/
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다."),
	TOKEN_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 생성 중 오류가 발생했습니다."),
	DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.");

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
