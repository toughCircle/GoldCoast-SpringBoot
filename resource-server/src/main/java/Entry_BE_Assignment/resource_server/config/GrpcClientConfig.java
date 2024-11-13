package Entry_BE_Assignment.resource_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import Entry_BE_Assignment.resource_server.grpc.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;

@Configuration
public class GrpcClientConfig {

	@Bean
	public ManagedChannel managedChannel() {
		return NettyChannelBuilder.forAddress("auth-server", 50051)  // 인증 서버의 호스트와 포트
			.usePlaintext()
			.disableServiceConfigLookUp()  // 통계 수집 비활성화
			.build();
	}

	@Bean
	public AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub(ManagedChannel channel) {
		return AuthServiceGrpc.newBlockingStub(channel);
	}
}