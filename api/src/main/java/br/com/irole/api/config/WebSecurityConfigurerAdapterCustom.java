package br.com.irole.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableWebSecurity
@EnableResourceServer
@Configuration
public class WebSecurityConfigurerAdapterCustom extends WebSecurityConfigurerAdapter {
	
	/*@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	*/
    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
    	auth.inMemoryAuthentication()
		.withUser("admin").password("admin").roles("ROLE");
    }
	
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    } 
    
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
     return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }    
	/*
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}    
	*/
}
