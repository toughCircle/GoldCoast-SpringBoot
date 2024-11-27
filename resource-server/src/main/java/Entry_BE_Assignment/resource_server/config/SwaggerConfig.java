package Entry_BE_Assignment.resource_server.config;

import java.util.Collections;

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
	public OpenAPI resourceOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Resource Server API")
				.description("Resource Server 관련 API 문서입니다.")
				.version("1.0"))
			.servers(Collections.singletonList(
				new Server().url("http://java.gold-coast.shop")
					.description("Production Server")
			))
			.components(new Components()
				.addSecuritySchemes("bearer-key",
					new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
			.addSecurityItem(new SecurityRequirement().addList("bearer-key"));
	}

}
