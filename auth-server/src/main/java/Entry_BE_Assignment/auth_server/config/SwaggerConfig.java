package Entry_BE_Assignment.auth_server.config;

import java.util.Collections;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi authApi() {
		return GroupedOpenApi.builder()
			.group("Auth API") // 인증 서버 API 그룹 이름
			.pathsToMatch("/api/auth/**")
			.build();
	}

	@Bean
	public OpenAPI authOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Auth Server API")
				.description("Auth Server 관련 API 문서입니다.")
				.version("1.0"))
			.servers(Collections.singletonList(
				new Server().url("http://java.gold-coast.shop/api/auth")
					.description("Production Auth Server")
			))
			.components(new Components()
				.addSecuritySchemes("bearer-key",
					new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
			.addSecurityItem(new SecurityRequirement().addList("bearer-key"));
	}

}
