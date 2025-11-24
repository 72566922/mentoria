package com.certus.mentoria.controller;

import com.certus.mentoria.model.user.Usuario;
import com.certus.mentoria.model.user.Rol;
import com.certus.mentoria.service.UsuarioService;
import com.certus.mentoria.service.RolService; // suponiendo que tienes RolService

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    // 🔹 Listar usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "usuarios/lista";
    }

    // 🔹 Formulario para nuevo usuario
    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.listarRoles());
        return "usuarios/form";
    }

    // 🔹 Guardar usuario
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario,
            @RequestParam(required = false) List<Long> rolesIds) {
        if (usuario.getId() == null) { // Nuevo usuario
            if (rolesIds == null || rolesIds.isEmpty()) {
                Rol rolAprendiz = rolService.buscarPorNombre("APRENDIZ");
                usuario.setRoles(Set.of(rolAprendiz));
            } else {
                usuario.setRoles(rolesIds.stream()
                        .map(id -> rolService.obtenerRolPorId(id).orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()));
            }
        } else {
            usuario.setRoles(rolesIds.stream()
                    .map(id -> rolService.obtenerRolPorId(id).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        }

        usuarioService.guardarUsuario(usuario);
        return "redirect:/usuarios";
    }

    // 🔹 Editar usuario
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.listarRoles());
        return "usuarios/form";
    }

    // 🔹 Eliminar usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}
