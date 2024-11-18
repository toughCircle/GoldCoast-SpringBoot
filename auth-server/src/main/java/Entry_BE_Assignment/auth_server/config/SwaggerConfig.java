package Entry_BE_Assignment.auth_server.config;

import java.util.Arrays;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.servers(Arrays.asList(
				new Server().url("http://localhost:8888").description("Auth Server"), // 인증 서버
				new Server().url("http://localhost:9999").description("Resource Server") // 자원 서버
			))
			.info(new Info()
				.title("Auth-Resource-Project API")
				.description("통합 API 문서")
				.version("1.0"));
	}

	@Bean
	public GroupedOpenApi authApi() {
		return GroupedOpenApi.builder()
			.group("Auth API")
			.pathsToMatch("/api/auth/**")
			.build();
	}
}
