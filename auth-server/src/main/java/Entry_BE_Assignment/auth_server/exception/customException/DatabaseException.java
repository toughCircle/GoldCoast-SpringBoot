package Entry_BE_Assignment.auth_server.exception.customException;

public class DatabaseException extends RuntimeException {
	public DatabaseException(String message) {
		super(message);
	}
}