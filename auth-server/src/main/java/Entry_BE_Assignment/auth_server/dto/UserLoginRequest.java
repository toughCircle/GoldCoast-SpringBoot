package Entry_BE_Assignment.auth_server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginRequest {

	@NotBlank(message = "사용자 이름은 필수입니다.")
	private String username;

	@NotBlank(message = "비밀번호는 필수입니다.")
	private String password;

}
