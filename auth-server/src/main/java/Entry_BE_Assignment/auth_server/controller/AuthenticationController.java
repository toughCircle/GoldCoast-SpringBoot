package Entry_BE_Assignment.auth_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Entry_BE_Assignment.auth_server.dto.BaseApiResponse;
import Entry_BE_Assignment.auth_server.enums.StatusCode;
import Entry_BE_Assignment.auth_server.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/refresh")
	public ResponseEntity<BaseApiResponse<Void>> refreshAccessToken(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Authentication 객체가 정상적으로 설정되지 않았다면 예외 처리
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(BaseApiResponse.of(StatusCode.UNAUTHORIZED));
		}

		// 필터에서 검증된 리프레시 토큰을 사용해 새로운 Access Token 생성
		String refreshToken = request.getHeader("Refresh-Token");
		String newAccessToken = authenticationService.refreshAccessToken(refreshToken);

		// 헤더에 새로운 Access Token 추가하고 본문에 상태 코드 및 메시지 포함
		return ResponseEntity.ok()
			.header("Access-Token", newAccessToken)
			.body(BaseApiResponse.of(StatusCode.TOKEN_REFRESH_SUCCESS));
	}

}
