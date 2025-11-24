package com.certus.mentoria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.certus.mentoria.model.user.Carrera;
import com.certus.mentoria.service.CarreraService;

@Controller
@RequestMapping("/carreras")
public class CarreraController {

    @Autowired
    private CarreraService carreraService;

    // 🔹 Listar carreras
    @GetMapping
    public String listarCarreras(Model model) {
        model.addAttribute("carreras", carreraService.listarCarreras());
        return "carreras/lista"; // carpeta + vista thymeleaf
    }

    // 🔹 Mostrar formulario para crear nueva carrera
    @GetMapping("/nueva")
    public String mostrarFormularioNuevaCarrera(Model model) {
        model.addAttribute("carrera", new Carrera());
        return "carreras/form";
    }

    // 🔹 Guardar carrera (crear o actualizar)
    @PostMapping("/guardar")
    public String guardarCarrera(@ModelAttribute Carrera carrera) {
        carreraService.guardarCarrera(carrera);
        return "redirect:/carreras";
    }

    // 🔹 Editar carrera existente
    @GetMapping("/editar/{id}")
    public String editarCarrera(@PathVariable Long id, Model model) {
        Carrera carrera = carreraService.obtenerCarreraPorId(id)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));
        model.addAttribute("carrera", carrera);
        return "carreras/form";
    }

    // 🔹 Eliminar carrera
    @GetMapping("/eliminar/{id}")
    public String eliminarCarrera(@PathVariable Long id) {
        carreraService.eliminarCarrera(id);
        return "redirect:/carreras";
    }
}
