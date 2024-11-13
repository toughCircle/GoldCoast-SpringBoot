package Entry_BE_Assignment.auth_server.controller;

import org.springframework.http.ResponseEntity;

import Entry_BE_Assignment.auth_server.dto.BaseApiResponse;
import Entry_BE_Assignment.auth_server.dto.LoginResponse;
import Entry_BE_Assignment.auth_server.dto.UserLoginRequest;
import Entry_BE_Assignment.auth_server.dto.UserRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Auth", description = "인증 관련 API")
public interface AuthenticationControllerDocs {

	@Operation(summary = "회원가입", description = "사용자가 회원가입을 위한 정보를 입력합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "회원가입이 완료되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
		@ApiResponse(responseCode = "409", description = "이미 사용 중인 계정입니다.")
	})
	BaseApiResponse<Void> registerUser(UserRegisterRequest request);

	@Operation(summary = "로그인", description = "사용자가 로그인을 위한 정보를 입력합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인에 성공했습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 인증 정보입니다.")
	})
	ResponseEntity<BaseApiResponse<LoginResponse>> login(UserLoginRequest request,
		HttpServletRequest httpServletRequest);

	@Operation(summary = "AccessToken 재발급", description = "토큰 재발급을 요청합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "토큰이 재발급되었습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 리프레시 토큰입니다."),
		@ApiResponse(responseCode = "401", description = "인증이 필요합니다."),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.")
	})
	ResponseEntity<BaseApiResponse<Void>> refreshAccessToken(HttpServletRequest request);
}
