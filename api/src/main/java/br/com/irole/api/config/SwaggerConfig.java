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

@Profile("oauth-security")
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("br.com.irole.api.resource"))
          .paths(PathSelectors.any())
          .build()
          .securityContexts(Lists.newArrayList(securityContext()))
          .securitySchemes(Lists.newArrayList(apiKey()));
    }
	
	private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }
	/*
	private OAuth securitySchema() {
		
        List<AuthorizationScope> authorizationScopeList = new ArrayList<>();
        authorizationScopeList.add(new AuthorizationScope("read", "read all"));
        authorizationScopeList.add(new AuthorizationScope("trust", "trust all"));
        authorizationScopeList.add(new AuthorizationScope("write", "access all"));

        List<GrantType> grantTypes = new ArrayList<>();
        GrantType creGrant = new ResourceOwnerPasswordCredentialsGrant("http://localhost:8089/oauth/token");

        grantTypes.add(creGrant);

        return new OAuth("oauth2schema", authorizationScopeList, grantTypes);

    }
	*/
	//TODO: Quando a versão 2.8+ do Swagger ter boa compatibilidade com JWT: migrar.
	@Bean
	SecurityConfiguration security() {
		return new SecurityConfiguration("vuejs", bCryptPasswordEncoder.encode("Irole@2019"), null,
				null,
				null,
				ApiKeyVehicle.HEADER, AUTHORIZATION_HEADER,
				null);
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex("/*"))
				.build();
	}
	
	List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
            new SecurityReference("JWT", authorizationScopes));
    }	
	
	private ApiInfo apiInfo() {
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
