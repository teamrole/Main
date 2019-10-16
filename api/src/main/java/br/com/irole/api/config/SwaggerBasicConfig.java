package br.com.irole.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Profile("basic-security")
@Configuration
public class SwaggerBasicConfig extends SwaggerConfig {

	@Bean
	public Docket greetingApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
		          .apis(RequestHandlerSelectors.basePackage("br.com.irole.api.resource"))
		          .build()
		          .apiInfo(apiInfo());

	}

}
