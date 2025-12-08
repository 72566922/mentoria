package com.certus.mentoria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Controller
public class HomeController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public HomeController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Comprobar si es un OidcUser
            if (authentication.getPrincipal() instanceof OidcUser) {
                OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                model.addAttribute("user", oidcUser); // Añadir OidcUser al modelo
            }
        }
        model.addAttribute("titulo", "Mentorías Certus");
        return "index"; // Thymeleaf se encargará de combinar layout + index
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof OidcUser) {
                OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                model.addAttribute("user", oidcUser); // Añadir OidcUser al modelo
            }
        }
        model.addAttribute("titulo", "Mentorías Certus");
        return "admin"; // Thymeleaf se encargará de combinar layout + admin
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Intentar revocar token de acceso en Google para evitar reuse automático de cuenta
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && authentication.getName() != null) {
                String principalName = authentication.getName();
                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("google", principalName);
                if (client != null) {
                    boolean hadToken = client.getAccessToken() != null;
                    System.out.println("Info: found authorized client for principal='" + principalName + "', hasAccessToken=" + hadToken);

                    if (hadToken) {
                        String accessToken = client.getAccessToken().getTokenValue();

                        RestTemplate rest = new RestTemplate();
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                        body.add("token", accessToken);
                        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

                        // Llamada al endpoint de revocación de Google
                        try {
                            rest.postForEntity("https://oauth2.googleapis.com/revoke", entity, String.class);
                            System.out.println("Info: token revoke request sent for principal='" + principalName + "'");
                        } catch (Exception ex) {
                            // No bloquear el logout si la revocación falla
                            System.out.println("Warning: unable to revoke Google token: " + ex.getMessage());
                        }
                    }

                    // Remove authorized client from the service to ensure it's not reused
                    try {
                        authorizedClientService.removeAuthorizedClient("google", principalName);
                        System.out.println("Info: removed authorized client for principal='" + principalName + "'");
                    } catch (Exception ex) {
                        System.out.println("Warning: unable to remove authorized client: " + ex.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Warning while attempting token revoke: " + e.getMessage());
        }

        // Cerrar la sesión en la aplicación
        request.logout();

        // Redirigir al login con parámetro logout para mostrar mensaje y permitir elegir cuenta
        return "redirect:/login?logout";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess(Model model) {
        System.out.println("¡Has cerrado sesión correctamente!");
        model.addAttribute("message", "¡Has cerrado sesión correctamente!");

        return "logout-success"; // Página de éxito
    }

}
