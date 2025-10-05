package com.certus.mentoria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {
        // Esto cargará el archivo src/main/resources/templates/index.html (Thymeleaf)
        return "index";
    }
}
