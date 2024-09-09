package Entry_BE_Assignment.auth_server.exception.customException;

public class DuplicateAccountException extends RuntimeException {
	public DuplicateAccountException(String message) {
		super(message);
	}
}