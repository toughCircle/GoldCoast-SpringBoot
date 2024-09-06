package Entry_BE_Assignment.auth_server.jwt;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtManager jwtManager;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String token = jwtManager.resolveToken(request);
		if (token != null && jwtManager.isValidToken(token)) {
			Long userId = jwtManager.getUserId(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

			// 인증 설정
			SecurityContextHolder.getContext().setAuthentication(
				jwtManager.getAuthentication(token, userDetails)
			);
		}

		filterChain.doFilter(request, response);
	}
}
