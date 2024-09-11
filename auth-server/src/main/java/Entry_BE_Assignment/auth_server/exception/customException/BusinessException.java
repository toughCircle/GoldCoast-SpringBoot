package Entry_BE_Assignment.auth_server.exception.customException;

import Entry_BE_Assignment.auth_server.enums.StatusCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private final StatusCode statusCode;
	private final String message;

	public BusinessException(StatusCode statusCode) {
		this.statusCode = statusCode;
		this.message = statusCode.getMessage();
	}

}