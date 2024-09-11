package Entry_BE_Assignment.auth_server.service;

import Entry_BE_Assignment.auth_server.entity.User;
import Entry_BE_Assignment.auth_server.enums.StatusCode;
import Entry_BE_Assignment.auth_server.exception.customException.BusinessException;
import Entry_BE_Assignment.auth_server.grpc.AuthServiceGrpc;
import Entry_BE_Assignment.auth_server.grpc.TokenRequest;
import Entry_BE_Assignment.auth_server.grpc.UserResponse;
import Entry_BE_Assignment.auth_server.jwt.JwtManager;
import Entry_BE_Assignment.auth_server.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

	private final UserRepository userRepository;
	private final JwtManager jwtManager;

	@Override
	public void validateToken(TokenRequest request, StreamObserver<UserResponse> responseObserver) {
		String token = request.getToken();

		if (!jwtManager.isValidToken(token)) {
			// 유효하지 않은 토큰에 대한 처리
			responseObserver.onError(new IllegalArgumentException("Invalid token"));
			return;
		}

		Long userId = jwtManager.getUserId(token);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(StatusCode.USER_NOT_FOUND));

		UserResponse response = UserResponse.newBuilder()
			.setUserId(user.getId())
			.setUsername(user.getUsername())
			.setEmail(user.getEmail())
			.setRole(user.getRole().toString())
			.build();

		// 응답 전송
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

}