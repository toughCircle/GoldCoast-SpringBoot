package Entry_BE_Assignment.auth_server.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Entry_BE_Assignment.auth_server.dto.UserRegisterRequest;
import Entry_BE_Assignment.auth_server.entity.User;
import Entry_BE_Assignment.auth_server.enums.StatusCode;
import Entry_BE_Assignment.auth_server.exception.customException.BusinessException;
import Entry_BE_Assignment.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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
			request.getPassword(),
			request.getEmail(),
			request.getRole());

		userRepository.save(user);

	}

	public User findUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(StatusCode.USER_NOT_FOUND));
	}

}
