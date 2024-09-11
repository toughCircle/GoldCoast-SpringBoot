package Entry_BE_Assignment.auth_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tokens {

	private String accessToken;
	private String refreshToken;

}
