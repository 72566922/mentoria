package com.certus.mentoria.controller.estudiante;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/agendar")
public class AgendarController {
    @GetMapping("/")
    public String agendar(Model model) {
        return "estudiante/inicio"; // Thymeleaf se encargará de combinar layout + index
    }
}
