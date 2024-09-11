package Entry_BE_Assignment.auth_server.exception.customException;

public class InvalidCredentialsException extends RuntimeException {
	public InvalidCredentialsException(String message) {
		super(message);
	}
}