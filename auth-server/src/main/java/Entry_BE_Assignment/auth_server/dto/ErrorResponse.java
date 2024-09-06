package Entry_BE_Assignment.auth_server.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

	private HttpStatus status;
	private String message;

	public ErrorResponse(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}