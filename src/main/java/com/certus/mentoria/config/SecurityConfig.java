package com.certus.mentoria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import com.certus.mentoria.service.CustomOAuth2UserService;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
            ClientRegistrationRepository clientRegistrationRepository) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        System.out.println("✅ Iniciando configuración de seguridad...");

        http
                // ✅ AUTORIZACIÓN DE RUTAS
                .authorizeHttpRequests(auth -> {
                    System.out.println("✅ Configurando autorización de rutas...");
                    auth
                            .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll() // Rutas públicas
                            .anyRequest().authenticated(); // Rutas privadas requieren autenticación
                })

                // ✅ LOGIN SOLO CON GOOGLE
                .oauth2Login(oauth -> {
                    System.out.println("✅ Configurando Login con Google...");
                    oauth
                            .loginPage("/login") // Página de login personalizada
                            .authorizationEndpoint(authz -> authz
                                .authorizationRequestResolver(
                                    new CustomAuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization")
                                )
                            )
                            .userInfoEndpoint(userInfo -> {
                                System.out.println("✅ Configurando servicio de usuario de Google...");
                                userInfo.oidcUserService(customOAuth2UserService); // Usar nuestro servicio
                                                                                   // personalizado
                            })
                            .defaultSuccessUrl("/", true); // Redirigir a la página de inicio después de un login
                                                           // exitoso
                })

                // ✅ LOGOUT
                .logout(logout -> {
                    logout
                        .logoutUrl("/logout") // Ruta de logout
                        .logoutSuccessUrl("/login?logout") // Redirige al login después de cerrar sesión
                        .clearAuthentication(true) // Limpia la autenticación
                        .invalidateHttpSession(true) // Invalida la sesión HTTP
                        .deleteCookies("JSESSIONID", "OAuth_Token", "google-auth-session", "G_AUTHUSER_H",
                            "G_AUTHUSER_HS", "G_AUTHUSER_S", "__Secure-ENID", "NID", "AEC", "3PC_MODE") // Elimina las cookies de sesión y Google
                        .permitAll(); // Permite el acceso sin autenticación
                })

                // ✅ CSRF (opcional, aquí lo dejamos activo por defecto)
                .csrf(csrf -> {
                    System.out.println("✅ CSRF activado.");
                    csrf.disable(); // Deshabilitar CSRF (opcional)
                });

        System.out.println("✅ Configuración de seguridad completada.");
        return http.build();
    }
}
