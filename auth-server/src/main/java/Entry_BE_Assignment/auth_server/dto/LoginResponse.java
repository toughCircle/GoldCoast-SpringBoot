package Entry_BE_Assignment.auth_server.dto;

import java.time.LocalDateTime;

import Entry_BE_Assignment.auth_server.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
	private Role role;
	private String email;
	private String username;
	private LocalDateTime createdAt;
}