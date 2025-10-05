package com.certus.mentoria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva protección CSRF (solo en desarrollo)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permite acceso a todas las rutas sin login
            )
            .formLogin(login -> login.disable()) // Desactiva formulario de login
            .httpBasic(basic -> basic.disable()); // Desactiva autenticación básica

        return http.build();
    }
}