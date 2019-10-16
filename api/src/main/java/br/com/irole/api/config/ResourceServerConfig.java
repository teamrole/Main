package br.com.irole.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {
	public void bbla() {
		//TODO retornar um AntMatchers comum aos profiles de autenticação
	}
}
