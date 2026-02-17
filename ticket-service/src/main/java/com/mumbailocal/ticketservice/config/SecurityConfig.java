package com.mumbailocal.ticketservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	    http
	        .csrf(csrf -> csrf.disable())
	        .sessionManagement(session ->
	            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        )
	        .authorizeHttpRequests(auth -> auth
	        	    .requestMatchers(
	        	        "/tickets/health",
	        	        "/",
	        	        "/home",
	        	        "/css/**",
	        	        "/js/**"
	        	    ).permitAll()
	        	    .anyRequest().authenticated()
	        	)
	        .httpBasic(httpBasic -> httpBasic.disable())
	        .formLogin(form -> form.disable());

	    http.addFilterBefore(new JwtAuthenticationFilter(),
	            UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}
}
