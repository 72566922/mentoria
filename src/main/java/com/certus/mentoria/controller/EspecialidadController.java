package com.certus.mentoria.controller;

import com.certus.mentoria.model.user.Especialidad;
import com.certus.mentoria.service.EspecialidadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    // 🔹 Listar
    @GetMapping
    public String listarEspecialidades(Model model) {
        model.addAttribute("especialidades", especialidadService.listarEspecialidades());
        return "especialidades/lista";
    }

    // 🔹 Formulario para nueva especialidad
    @GetMapping("/nueva")
    public String nuevaEspecialidad(Model model) {
        model.addAttribute("especialidad", new Especialidad());
        return "especialidades/form";
    }

    // 🔹 Guardar (crear o actualizar)
    @PostMapping("/guardar")
    public String guardarEspecialidad(@ModelAttribute Especialidad especialidad) {
        especialidadService.guardarEspecialidad(especialidad);
        return "redirect:/especialidades";
    }

    // 🔹 Editar
    @GetMapping("/editar/{id}")
    public String editarEspecialidad(@PathVariable Long id, Model model) {
        Especialidad especialidad = especialidadService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        model.addAttribute("especialidad", especialidad);
        return "especialidades/form";
    }

    // 🔹 Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminarEspecialidad(@PathVariable Long id) {
        especialidadService.eliminar(id);
        return "redirect:/especialidades";
    }
}
