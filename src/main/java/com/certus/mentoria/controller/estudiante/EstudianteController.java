package com.certus.mentoria.controller.estudiante;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.certus.mentoria.model.sesion.Estado;
import com.certus.mentoria.model.sesion.Sesion;
import com.certus.mentoria.model.user.PerfilAprendiz;
import com.certus.mentoria.model.user.PerfilMentor;
import com.certus.mentoria.model.user.Usuario;
import com.certus.mentoria.repository.PerfilAprendizRepository;
import com.certus.mentoria.repository.PerfilMentorRepository;
import com.certus.mentoria.repository.SesionRepository;
import com.certus.mentoria.repository.UsuarioRepository;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private final PerfilMentorRepository perfilMentorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilAprendizRepository perfilAprendizRepository;
    private final SesionRepository sesionRepository;

    public EstudianteController(
            PerfilMentorRepository perfilMentorRepository,
            UsuarioRepository usuarioRepository,
            PerfilAprendizRepository perfilAprendizRepository,
            SesionRepository sesionRepository) {

        this.perfilMentorRepository = perfilMentorRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilAprendizRepository = perfilAprendizRepository;
        this.sesionRepository = sesionRepository;
    }

    // 🏠 Página principal del estudiante (dashboard)
    @GetMapping("/inicio")
    public String inicioEstudiante(Model model) {
        // Por ahora, usuario fijo con id 7
        Usuario usuario = usuarioRepository.findById(3L).orElse(null);
        model.addAttribute("usuario", usuario);

        // Lista de mentores disponibles
        List<PerfilMentor> mentores = perfilMentorRepository.findAllMentores();
        model.addAttribute("mentores", mentores);

        // Sesiones agendadas del aprendiz
        List<Sesion> sesiones = sesionRepository.findByAprendizUsuarioId(3L);
        model.addAttribute("sesiones", sesiones);

        // Objeto vacío para formulario
        model.addAttribute("sesion", new Sesion());

        String nombreUsuario = usuario != null ? usuario.getNombre() : "Aprendiz";
        model.addAttribute("titulo", "Bienvenido, " + nombreUsuario);

        return "estudiante/inicio";
    }

    // 🔍 Buscar mentores
    @GetMapping("/buscar_mentor")
    public String buscarMentores(Model model) {
        List<PerfilMentor> mentores = perfilMentorRepository.findAllMentores();
        model.addAttribute("mentores", mentores);
        return "estudiante/buscar_mentor";
    }

    // 👩‍🏫 Ver perfil del mentor
    @GetMapping("/ver_perfil_mentor")
    public String verPerfilMentor(Model model) {
        model.addAttribute("titulo", "Perfil del Mentor");
        return "estudiante/ver_perfil_mentor";
    }

    // 🧾 Mis mentorías
    @GetMapping("/mis_mentorias")
    public String misMentorias(Model model) {
        List<Sesion> sesiones = sesionRepository.findByAprendizUsuarioId(7L);
        model.addAttribute("sesiones", sesiones);
        model.addAttribute("titulo", "Mis Mentorías");
        return "estudiante/mis_mentorias";
    }

    // ⚙️ Perfil del estudiante
    @GetMapping("/perfil")
    public String perfilEstudiante(Model model) {
        model.addAttribute("titulo", "Mi Perfil");
        return "estudiante/perfil";
    }

    // 📅 Agendar nueva sesión
    @PostMapping("/agendar")
    public String agendarSesion(
            @RequestParam("mentorId") Long mentorId,
            @RequestParam("tema") String tema,
            @RequestParam("fecha") String fecha,
            @RequestParam("hora") String hora) {

        PerfilMentor mentor = perfilMentorRepository.findById(mentorId).orElseThrow();

        // Por ahora se asume aprendiz fijo (usuario 7)
        Usuario usuario = usuarioRepository.findById(7L).orElseThrow();
        PerfilAprendiz aprendiz = perfilAprendizRepository.findByUsuario(usuario);

        LocalDateTime fechaHora = LocalDateTime.parse(fecha + "T" + hora);

        // Crear y guardar sesión
        Sesion sesion = new Sesion();
        sesion.setMentor(mentor);
        sesion.setAprendiz(aprendiz);
        sesion.setTema(tema);
        sesion.setFechaHora(fechaHora);
        sesion.setEstado(Estado.PENDIENTE);

        sesionRepository.save(sesion);

        return "redirect:/estudiante/mis_mentorias";
    }
}
