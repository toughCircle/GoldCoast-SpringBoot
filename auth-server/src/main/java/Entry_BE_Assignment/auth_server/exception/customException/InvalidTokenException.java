package Entry_BE_Assignment.auth_server.exception.customException;

public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException(String message) {
		super(message);
	}
}