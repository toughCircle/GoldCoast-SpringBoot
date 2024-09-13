package Entry_BE_Assignment.auth_server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserLoginRequest {

	@Schema(description = "사용자 이메일")
	@Email(message = "유효한 이메일 형식이 아닙니다.")
	@NotEmpty(message = "이메일은 필수 입력 값입니다.")
	private String email;

	@Schema(description = "비밀번호")
	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
	private String password;

}
