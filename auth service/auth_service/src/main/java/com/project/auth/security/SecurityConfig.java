/*package com.project.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF ONLY for now (UI testing)
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // Allow UI pages
                .requestMatchers(
                    "/login",
                    "/register",
                    "/",
                    "/error",

                    // Allow backend auth APIs
                    "/auth/**",

                    // Allow static resources (future CSS/JS)
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()

                // Everything else requires authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }
}*/

package com.project.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/error"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
