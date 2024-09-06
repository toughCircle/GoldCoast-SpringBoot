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

	private final Key secretKey;
	private final JwtParser jwtParser;  // JWT 파싱기 모듈화

	@Value("${spring.security.jwt.expiration}")
	private long tokenValidity;

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
			// 토큰 만료 시 처리 TODO 예외처리
			// throw new TokenExpiredException("Token expired");
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	// Http Header 에서 token 추출
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);  // "Bearer " 접두어 제거 후 JWT 토큰 반환
		}
		return null;  // 토큰이 없는 경우 null 반환
	}

	public Authentication getAuthentication(String token, UserDetails userDetails) {
		return new UsernamePasswordAuthenticationToken(
			userDetails, null, userDetails.getAuthorities());
	}

}
