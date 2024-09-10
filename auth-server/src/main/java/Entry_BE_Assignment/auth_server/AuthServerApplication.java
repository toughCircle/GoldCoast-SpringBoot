package Entry_BE_Assignment.auth_server;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

import Entry_BE_Assignment.auth_server.jwt.JwtManager;
import Entry_BE_Assignment.auth_server.repository.UserRepository;
import Entry_BE_Assignment.auth_server.service.AuthServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaAuditing
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

}

@Component
class GrpcServerRunner {

	@Value("${grpc.server.port}")
	private int port;

	private final UserRepository userRepository;
	private final JwtManager jwtManager;

	public GrpcServerRunner(UserRepository userRepository, JwtManager jwtManager) {
		this.userRepository = userRepository;
		this.jwtManager = jwtManager;
	}

	@PostConstruct
	public void startGrpcServer() {
		new Thread(() -> {
			try {
				Server server = ServerBuilder.forPort(port)
					.addService(new AuthServiceImpl(userRepository, jwtManager))
					.build()
					.start();

				System.out.println("Auth gRPC Server started on port " + port);
				server.awaitTermination();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
}