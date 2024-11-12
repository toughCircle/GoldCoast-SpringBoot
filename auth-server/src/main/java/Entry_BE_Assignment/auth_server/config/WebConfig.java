package Entry_BE_Assignment.auth_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/auth/**")
			.allowedOrigins("http://java.gold-coast.shop")
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
	}
}

