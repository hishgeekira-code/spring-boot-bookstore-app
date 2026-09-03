package com.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.bookstore.security.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final LoginSuccessHandler loginSuccessHandler;
	
	public SecurityConfig(LoginSuccessHandler loginSuccessHandler) {
		this.loginSuccessHandler = loginSuccessHandler;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/shop", "/shop/**", "/login", "/register", "/css/**", "/js/**", "/images/**", "/api/health", "/error").permitAll()
				.requestMatchers("/admin/**", "/books", "/authors", "/categories").hasRole("ADMIN")
				.requestMatchers("/api/users/**", "/api/books/**", "/api/authors/**", "/api/categories/**")
				.hasRole("ADMIN").requestMatchers("/customer/**").hasRole("CUSTOMER").anyRequest().authenticated());

		http.formLogin(
				form -> form
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.successHandler(loginSuccessHandler)
				.failureUrl("/login?error")
				.permitAll()
				);

		http.logout(
				logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.permitAll()
				);

		return http.build();
	}
}
