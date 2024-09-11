package Entry_BE_Assignment.auth_server.dto;

import Entry_BE_Assignment.auth_server.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRegisterRequest {

	private String username;
	@Email(message = "유효한 이메일 형식이어야 합니다.")
	private String email;
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|`~])[A-Za-z\\d!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|`~]{5,}$",
		message = "비밀번호는 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 2개 이상을 포함하고, 최소 5자리 이상이어야 합니다."
	)
	private String password;
	private Role role;

}
