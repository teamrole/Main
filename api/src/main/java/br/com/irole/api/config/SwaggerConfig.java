package br.com.irole.api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	
	@Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;
	
	protected ApiInfo apiInfo() {
	    return new ApiInfoBuilder()
	            .title("API IROLE")
	            .description("Aplicação para divisor de contas")
	            .version("1.0.0")
	            .license("IROLE 2019")
	            .licenseUrl("https://github.com/teamrole/Main.git/")
	            .contact(new Contact("Daniel Junior", "https://github.com/JrDani", "danjun48@gmail.com"))
	            .build();
	}
	
}
