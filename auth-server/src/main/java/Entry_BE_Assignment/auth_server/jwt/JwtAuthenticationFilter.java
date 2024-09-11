package Entry_BE_Assignment.auth_server.jwt;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtManager jwtManager;
	private final CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		try {
			String token = jwtManager.resolveToken(request);
			if (token != null && jwtManager.isValidToken(token)) {
				Long userId = jwtManager.getUserId(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

				// 인증 설정
				SecurityContextHolder.getContext().setAuthentication(
					jwtManager.getAuthentication(token, userDetails)
				);
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Invalid request: " + e.getMessage());
			return;
		}

		filterChain.doFilter(request, response);
	}

}
