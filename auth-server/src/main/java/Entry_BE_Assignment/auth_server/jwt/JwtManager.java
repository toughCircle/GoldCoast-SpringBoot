package Entry_BE_Assignment.auth_server.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import Entry_BE_Assignment.auth_server.enums.StatusCode;
import Entry_BE_Assignment.auth_server.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtManager {

	private static final String CLAIM_USER_NAME = "username";
	private static final String CLAIM_ROLE = "role";
	private static final String CLAIM_IP = "ip";
	private static final String CLAIM_USER_AGENT = "userAgent";

	private final Key secretKey;
	private final JwtParser jwtParser;  // JWT 파싱기 모듈화

	@Value("${spring.security.jwt.expiration}")
	private long tokenValidity;

	@Value("${spring.security.jwt.refresh-expiration}")
	private long refreshTokenValidity;

	public JwtManager(@Value("${spring.security.jwt.secret}") String secret) {
		byte[] keyBytes = Base64.getDecoder().decode(secret);
		this.secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
		this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();  // 파싱기 생성
	}

	// JWT 토큰 생성
	public String generateToken(Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		Long userId = userDetails.getUserId();
		String username = userDetails.getUsername();
		String role = userDetails.getRole().name();

		Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
		claims.put(CLAIM_USER_NAME, username);
		claims.put(CLAIM_ROLE, role);

		Date now = new Date();
		Date validity = new Date(now.getTime() + tokenValidity * 1000);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	// Refresh Token 생성
	public String generateRefreshToken(Authentication authentication, String ipAddress, String userAgent) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		Long userId = userDetails.getUserId();

		Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
		claims.put(CLAIM_IP, ipAddress);
		claims.put(CLAIM_USER_AGENT, userAgent);

		Date now = new Date();
		Date validity = new Date(now.getTime() + refreshTokenValidity * 1000);  // Refresh Token의 유효 기간 설정

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	// 공통 파싱 메서드: JWT 토큰에서 Claims 객체를 추출
	private Claims getClaims(String token) {
		return jwtParser.parseClaimsJws(token).getBody();
	}

	// JWT 토큰에서 userId 추출
	public Long getUserId(String token) {
		return Long.parseLong(getClaims(token).getSubject());
	}

	// JWT 토큰에서 username 추출
	public String getUsername(String token) {
		return getClaims(token).get(CLAIM_USER_NAME, String.class);
	}

	// JWT에서 역할 추출
	public String getRoleFromToken(String token) {
		return getClaims(token).get(CLAIM_ROLE, String.class);
	}

	// JWT 유효성 검증
	public boolean isValidToken(String token) {
		try {
			Claims claims = getClaims(token);
			return !claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			// 토큰 만료 시 처리
			throw new TokenExpiredException(StatusCode.EXPIRED_ACCESS_TOKEN.getMessage());
		} catch (Exception e) {
			return false;
		}
	}

	// Refresh Token 검증
	public boolean isValidateRefreshToken(String token, String currentIpAddress, String currentUserAgent) {
		try {
			Claims claims = getClaims(token);

			// 토큰에 포함된 IP 주소와 현재 요청의 IP 주소 비교
			String tokenIp = claims.get("ip", String.class);
			String tokenUserAgent = claims.get("userAgent", String.class);

			if (!tokenIp.equals(currentIpAddress) || !tokenUserAgent.equals(currentUserAgent)) {
				log.error("토큰의 IP 또는 사용자 에이전트 정보가 일치하지 않음");
				return false;  // IP나 사용자 에이전트 정보가 다를 경우 토큰 무효화
			}

			return true;
		} catch (Exception e) {
			log.error("JWT 유효성 검사 실패: {}", token, e);
			return false;
		}
	}

	// Http Header 에서 token 추출
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);  // "Bearer " 접두어 제거 후 JWT 토큰 반환
		}
		return null;
	}

	public Authentication getAuthentication(String token, UserDetails userDetails) {
		return new UsernamePasswordAuthenticationToken(
			userDetails, null, userDetails.getAuthorities());
	}

}
