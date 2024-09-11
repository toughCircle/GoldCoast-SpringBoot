package Entry_BE_Assignment.resource_server.exception;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import Entry_BE_Assignment.resource_server.dto.BaseApiResponse;
import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.exception.customException.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<BaseApiResponse<Void>> handleBusinessException(BusinessException e) {
		log.error(e.getMessage(), e);
		return buildErrorResponse(e.getStatusCode());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseApiResponse<Map<String, String>>> handleValidationExceptions(
		MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		// 모든 필드 에러를 순회하여 필드명과 에러 메시지를 저장
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String fieldName = ((FieldError)error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		}

		// 필드별로 모은 에러 메시지를 반환
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(BaseApiResponse.of(StatusCode.VALIDATION_FAILED, errors));
	}

	// 접근 권한 관련 예외 처리
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<BaseApiResponse<Void>> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
		log.error("AccessDeniedException: {}", ex.getMessage());
		return buildErrorResponse(StatusCode.FORBIDDEN);
	}

	// 기본 예외 처리기 (예상치 못한 예외)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseApiResponse<Void>> handleGenericException(Exception ex, WebRequest request) {
		log.error("UnexpectedException: {}", ex.getMessage(), ex);
		return buildErrorResponse(StatusCode.INTERNAL_SERVER_ERROR);
	}

	// 에러 응답을 빌드하는 유틸리티 메서드
	private ResponseEntity<BaseApiResponse<Void>> buildErrorResponse(StatusCode statusCode) {
		return ResponseEntity.status(statusCode.getHttpStatus())
			.body(BaseApiResponse.of(statusCode.getHttpStatus()));
	}
}