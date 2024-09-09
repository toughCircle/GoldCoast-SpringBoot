package Entry_BE_Assignment.auth_server.service;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Entry_BE_Assignment.auth_server.entity.RefreshToken;
import Entry_BE_Assignment.auth_server.entity.User;
import Entry_BE_Assignment.auth_server.enums.StatusCode;
import Entry_BE_Assignment.auth_server.exception.customException.BusinessException;
import Entry_BE_Assignment.auth_server.jwt.JwtManager;
import Entry_BE_Assignment.auth_server.repository.RefreshTokenRepository;
import Entry_BE_Assignment.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtManager jwtManager;

	@Transactional
	public void saveRefreshToken(String username, String refreshToken) {
		LocalDateTime expireDate = LocalDateTime.now().plusDays(7);
		RefreshToken token = new RefreshToken(username, refreshToken, expireDate);  // 7일간 유효

		refreshTokenRepository.deleteByUsername(username);// 기존 토큰 삭제
	}

	public String refreshAccessToken(String refreshToken, String currentIpAddress, String currentUserAgent) {
		if (jwtManager.isValidateRefreshToken(refreshToken, currentIpAddress, currentUserAgent)) {
			Long userId = jwtManager.getUserId(refreshToken);
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(StatusCode.USER_NOT_FOUND));

			return jwtManager.generateToken((Authentication)user);
		} else {
			throw new BusinessException(StatusCode.INVALID_REFRESH_TOKEN);
		}
	}

}
