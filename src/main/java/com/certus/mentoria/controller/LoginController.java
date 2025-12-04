package com.certus.mentoria.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Controller
public class LoginController {

    // Página personalizada de login
    @GetMapping("/login")
    public String login() {
        return "login"; // debe existir login.html en /templates
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, 
                            @AuthenticationPrincipal OAuth2User user) {

        if (user != null) {
            model.addAttribute("name", user.getAttribute("name"));
            model.addAttribute("email", user.getAttribute("email"));
        }

        return "index"; 
    }

}
