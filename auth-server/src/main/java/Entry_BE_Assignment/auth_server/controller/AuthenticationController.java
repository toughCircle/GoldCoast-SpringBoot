package Entry_BE_Assignment.auth_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Entry_BE_Assignment.auth_server.dto.BaseApiResponse;
import Entry_BE_Assignment.auth_server.dto.UserRegisterRequest;
import Entry_BE_Assignment.auth_server.enums.StatusCode;
import Entry_BE_Assignment.auth_server.service.AuthenticationService;
import Entry_BE_Assignment.auth_server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final UserService userService;

	@PostMapping("/refresh")
	public ResponseEntity<BaseApiResponse<Void>> refreshAccessToken(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Authentication 객체가 정상적으로 설정되지 않았다면 예외 처리
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(BaseApiResponse.of(StatusCode.UNAUTHORIZED));
		}

		String ip = getClientIp(request);

		// 필터에서 검증된 리프레시 토큰을 사용해 새로운 Access Token 생성
		String refreshToken = request.getHeader("Refresh-Token");
		String newAccessToken = authenticationService.refreshAccessToken(refreshToken, ip,
			request.getHeader("User-Agent"));

		// 헤더에 새로운 Access Token 추가하고 본문에 상태 코드 및 메시지 포함
		return ResponseEntity.ok()
			.header("Access-Token", newAccessToken)
			.body(BaseApiResponse.of(StatusCode.TOKEN_REFRESH_SUCCESS));
	}

	public String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();  // 프록시를 통하지 않은 경우 IP 주소
		}
		return ip;
	}

	@PostMapping("/register")
	public BaseApiResponse<Void> registerUser(@Valid @RequestBody UserRegisterRequest request) {
		userService.registerNewUser(request);
		return BaseApiResponse.of(StatusCode.SIGN_UP_SUCCESS);
	}

}
