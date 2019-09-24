package br.com.irole.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.irole.api.config.property.ApiIroleProperty;

@SpringBootApplication
@EnableConfigurationProperties(ApiIroleProperty.class)
public class ApiIroleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiIroleApplication.class, args);
	}

}
