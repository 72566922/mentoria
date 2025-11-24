package com.certus.mentoria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("titulo", "Mentorías Certus");
        return "index"; // Thymeleaf se encargará de combinar layout + index
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("titulo", "Mentorías Certus");
        return "admin"; // Thymeleaf se encargará de combinar layout + index
    }
}
