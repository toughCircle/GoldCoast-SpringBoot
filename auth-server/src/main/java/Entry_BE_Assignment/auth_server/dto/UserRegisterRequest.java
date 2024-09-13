package Entry_BE_Assignment.auth_server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRegisterRequest {

	@Schema(description = "사용자 이름")
	@NotEmpty(message = "사용자 이름은 필수 입력 값입니다.")
	private String username;

	@Schema(description = "사용자 이메일")
	@Email(message = "유효한 이메일 형식이 아닙니다.")
	@NotEmpty(message = "이메일은 필수 입력 값입니다.")
	private String email;

	@Schema(description = "비밀번호")
	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
	@Pattern(
		regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
		message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
	)
	private String password;

}
