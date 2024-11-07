package Entry_BE_Assignment.auth_server.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Entry_BE_Assignment.auth_server.entity.User;
import Entry_BE_Assignment.auth_server.enums.StatusCode;
import Entry_BE_Assignment.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 데이터베이스에서 사용자 조회
		User user = userRepository.findByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException(StatusCode.USER_NOT_FOUND.name()));

		// User 엔티티를 CustomUserDetails 로 변환하여 반환
		return new CustomUserDetails(user);
	}
}
