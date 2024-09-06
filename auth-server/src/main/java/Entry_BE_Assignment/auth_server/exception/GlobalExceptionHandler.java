package Entry_BE_Assignment.auth_server.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import Entry_BE_Assignment.auth_server.dto.BaseApiResponse;
import Entry_BE_Assignment.auth_server.enums.StatusCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	// 인증 실패 예외 처리
	@ExceptionHandler(InvalidTokenException.class)
	public BaseApiResponse<Void> handleInvalidToken(InvalidTokenException ex, WebRequest request) {
		log.error("InvalidTokenException: {}", ex.getMessage());
		return buildErrorResponse(StatusCode.INVALID_TOKEN);
	}

	// 리프레시 토큰 만료 예외 처리
	@ExceptionHandler(TokenExpiredException.class)
	public BaseApiResponse<Void> handleTokenExpired(TokenExpiredException ex, WebRequest request) {
		log.error("TokenExpiredException: {}", ex.getMessage());
		return buildErrorResponse(StatusCode.EXPIRED_REFRESH_TOKEN);
	}

	// 사용자 인증 정보 불일치 예외 처리
	@ExceptionHandler(InvalidCredentialsException.class)
	public BaseApiResponse<Void> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
		log.error("InvalidCredentialsException: {}", ex.getMessage());
		return buildErrorResponse(StatusCode.INVALID_CREDENTIALS);
	}

	// 계정 중복 예외 처리
	@ExceptionHandler(DuplicateAccountException.class)
	public BaseApiResponse<Void> handleDuplicateAccount(DuplicateAccountException ex, WebRequest request) {
		log.error("DuplicateAccountException: {}", ex.getMessage());
		return buildErrorResponse(StatusCode.DUPLICATE_ACCOUNT);
	}

	// 인증 관련 예외 처리
	@ExceptionHandler(AuthenticationException.class)
	public BaseApiResponse<Void> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
		log.error("AuthenticationException: {}", ex.getMessage());
		return buildErrorResponse(StatusCode.UNAUTHORIZED);
	}

	// 접근 권한 관련 예외 처리
	@ExceptionHandler(AccessDeniedException.class)
	public BaseApiResponse<Void> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
		log.error("AccessDeniedException: {}", ex.getMessage());
		return buildErrorResponse(StatusCode.FORBIDDEN);
	}

	// 데이터베이스 오류 처리
	@ExceptionHandler(DatabaseException.class)
	public BaseApiResponse<Void> handleDatabaseException(DatabaseException ex, WebRequest request) {
		log.error("DatabaseException: {}", ex.getMessage());
		return buildErrorResponse(StatusCode.DATABASE_ERROR);
	}

	// 기본 예외 처리기 (예상치 못한 예외)
	@ExceptionHandler(Exception.class)
	public BaseApiResponse<Void> handleGenericException(Exception ex, WebRequest request) {
		log.error("UnexpectedException: {}", ex.getMessage(), ex);
		return buildErrorResponse(StatusCode.INTERNAL_SERVER_ERROR);
	}

	// 에러 응답을 빌드하는 유틸리티 메서드
	private BaseApiResponse<Void> buildErrorResponse(StatusCode statusCode) {
		return BaseApiResponse.of(statusCode.getHttpStatus());
	}
}