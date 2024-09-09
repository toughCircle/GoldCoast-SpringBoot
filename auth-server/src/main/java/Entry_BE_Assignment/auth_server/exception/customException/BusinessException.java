package Entry_BE_Assignment.auth_server.exception.customException;

import Entry_BE_Assignment.auth_server.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

	private final StatusCode statusCode;
	private String message;

	public BusinessException(StatusCode statusCode) {
		this.statusCode = statusCode;
		this.message = statusCode.getMessage();
	}
}