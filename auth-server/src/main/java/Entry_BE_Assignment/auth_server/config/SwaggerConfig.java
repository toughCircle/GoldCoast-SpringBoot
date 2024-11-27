package Entry_BE_Assignment.auth_server.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI authOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Auth Server API") // API 이름
				.description("Auth Server 관련 API 문서입니다.") // 설명
				.version("1.0")) // 버전
			.servers(Collections.singletonList(
				new Server().url("http://java.gold-coast.shop") // 인증 서버의 기본 URL 설정
					.description("Production Auth Server")
			)); // 모든 API 호출에 보안 스키마 적용
	}

}
