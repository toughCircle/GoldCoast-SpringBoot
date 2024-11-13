package Entry_BE_Assignment.auth_server.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Entry_BE_Assignment.auth_server.dto.LoginResponse;
import Entry_BE_Assignment.auth_server.dto.Tokens;
import Entry_BE_Assignment.auth_server.dto.UserLoginRequest;
import Entry_BE_Assignment.auth_server.dto.UserRegisterRequest;
import Entry_BE_Assignment.auth_server.entity.User;
import Entry_BE_Assignment.auth_server.enums.StatusCode;
import Entry_BE_Assignment.auth_server.exception.customException.BusinessException;
import Entry_BE_Assignment.auth_server.jwt.JwtManager;
import Entry_BE_Assignment.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtManager jwtManager;
	private final PasswordEncoder passwordEncoder;

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

	@Transactional
	public void registerNewUser(UserRegisterRequest request) {

		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new BusinessException(StatusCode.DUPLICATE_ACCOUNT);
		}
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new BusinessException(StatusCode.DUPLICATE_ACCOUNT);
		}

		String encryptedPassword = passwordEncoder.encode(request.getPassword());

		User user = User.registerUser(
			request.getUsername(),
			encryptedPassword,
			request.getEmail(),
			request.getRole());

		userRepository.save(user);

	}

	public Tokens login(UserLoginRequest request, String ipAddress, String userAgent) {
		try {
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
			);

			// 인증된 사용자 정보에서 UserDetails 가져오기
			UserDetails userDetails = (UserDetails)authentication.getPrincipal();

			User user = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new BusinessException(StatusCode.USER_NOT_FOUND));

			// JWT Access Token 생성
			String accessToken = jwtManager.generateToken(authentication);

			// JWT Refresh Token 생성
			String refreshToken = jwtManager.generateRefreshToken(authentication, ipAddress, userAgent);

			LoginResponse loginResponse = new LoginResponse(user.getRole(), user.getEmail(), user.getUsername(),
				user.getCreatedAt());
			return new Tokens(accessToken, refreshToken, loginResponse);

		} catch (BadCredentialsException e) {
			throw new BusinessException(StatusCode.INVALID_CREDENTIALS);
		}
	}
}
