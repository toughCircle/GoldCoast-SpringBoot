package Entry_BE_Assignment.auth_server.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Entry_BE_Assignment.auth_server.entity.RefreshToken;
import Entry_BE_Assignment.auth_server.jwt.JwtManager;
import Entry_BE_Assignment.auth_server.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtManager jwtManager;

	@Transactional
	private void saveRefreshToken(String username, String refreshToken) {
		LocalDateTime expireDate = LocalDateTime.now().plusDays(7);
		RefreshToken token = new RefreshToken(username, refreshToken, expireDate);  // 7일간 유효

		refreshTokenRepository.deleteByUsername(username);  // 기존 토큰 삭제
	}

}
