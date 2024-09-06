package Entry_BE_Assignment.auth_server.exception;

public class TokenExpiredException extends RuntimeException {
	public TokenExpiredException(String message) {
		super(message);
	}
}