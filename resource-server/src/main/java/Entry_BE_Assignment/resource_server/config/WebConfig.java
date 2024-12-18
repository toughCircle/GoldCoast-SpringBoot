package Entry_BE_Assignment.resource_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
			.allowedOrigins("http://java.gold-coast.shop", "http://localhost:3000")
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
			.allowedHeaders("Authorization", "Content-Type", "Refresh-Token")
			.exposedHeaders("Authorization", "Refresh-Token");
	}
}
