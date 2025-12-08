package com.certus.mentoria.controller.estudiante;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.certus.mentoria.model.sesion.Estado;
import com.certus.mentoria.model.sesion.Sesion;
import com.certus.mentoria.model.sesion.Tema;
import com.certus.mentoria.model.user.PerfilAprendiz;
import com.certus.mentoria.model.user.PerfilMentor;
import com.certus.mentoria.model.user.Usuario;
import com.certus.mentoria.repository.PerfilAprendizRepository;
import com.certus.mentoria.repository.PerfilMentorRepository;
import com.certus.mentoria.repository.SesionRepository;
import com.certus.mentoria.repository.TemaRepository;
import com.certus.mentoria.repository.UsuarioRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private final PerfilMentorRepository perfilMentorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilAprendizRepository perfilAprendizRepository;
    private final SesionRepository sesionRepository;
    private final TemaRepository temaRepository;

    public EstudianteController(
            PerfilMentorRepository perfilMentorRepository,
            UsuarioRepository usuarioRepository,
            PerfilAprendizRepository perfilAprendizRepository,
            SesionRepository sesionRepository,
            TemaRepository temaRepository) {

        this.perfilMentorRepository = perfilMentorRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilAprendizRepository = perfilAprendizRepository;
        this.sesionRepository = sesionRepository;
        this.temaRepository = temaRepository;
    }

    @GetMapping("/inicio")
    public String inicioEstudiante(Model model, @AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);

        Long userId = usuario.getId();

        PerfilAprendiz aprendiz = perfilAprendizRepository.findByUsuario(usuario);
        model.addAttribute("aprendiz", aprendiz);

        List<PerfilMentor> mentores = perfilMentorRepository.findAllMentores();
        model.addAttribute("mentores", mentores);

        List<Sesion> sesiones = sesionRepository.findByAprendizUsuarioId(userId);
        model.addAttribute("sesiones", sesiones);

        List<Tema> temas = temaRepository.findAll();
        model.addAttribute("temas", temas);

        model.addAttribute("sesion", new Sesion());

        return "estudiante/inicio";
    }

    @PostMapping("/agendar")
    public String agendarSesion(
            @RequestParam Map<String, String> allParams, // captura todos los parámetros como mapa
            @RequestParam("mentorId") Long mentorId,
            @RequestParam("temaIds") List<Long> temaIds,
            @RequestParam("fecha") String fecha,
            @RequestParam("hora") String hora,
            Model model, @AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        PerfilAprendiz aprendiz = perfilAprendizRepository.findByUsuario(usuario);

        PerfilMentor mentor = perfilMentorRepository.findById(mentorId).orElseThrow();

        // Buscar lista de temas seleccionados
        List<Tema> temasSeleccionados = temaRepository.findAllById(temaIds);

        LocalDateTime fechaHora = LocalDateTime.parse(fecha + "T" + hora);

        System.out.println("=== Todos los parámetros recibidos ===");
        allParams.forEach((k, v) -> System.out.println(k + " : " + v));

        System.out.println("mentorId = " + mentorId);
        System.out.println("temaIds = " + temaIds);
        System.out.println("fecha = " + fecha);
        System.out.println("hora = " + hora);
        System.out.println("ID del aprendiz = " + aprendiz.getId());

        

        Sesion sesion = new Sesion();
        sesion.setMentor(mentor);
        sesion.setAprendiz(aprendiz);
        sesion.setTemas(temasSeleccionados);
        sesion.setFechaHora(fechaHora);
        sesion.setEstado(Estado.PENDIENTE);

        sesionRepository.save(sesion);

        return "redirect:/estudiante/inicio";
    }


    @GetMapping("/temas_por_mentor")
    @ResponseBody
    public List<Map<String, Object>> obtenerTemasPorMentor(@RequestParam Long mentorId) {
        PerfilMentor mentor = perfilMentorRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor no encontrado"));

        Long especialidadId = mentor.getEspecialidad().getId();
        List<Tema> temas = temaRepository.findByEspecialidadId(especialidadId);

        return temas.stream()
                .map(t -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", t.getId());
                    map.put("nombre", t.getNombre());
                    map.put("especialidad", t.getEspecialidad().getNombre());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/subirFoto")
    public String subirFoto(
            @RequestParam("foto") MultipartFile file,
            @AuthenticationPrincipal OAuth2User principal) {
        
        if (file.isEmpty()) {
            return "redirect:/estudiante/inicio?error=archivo_vacio";
        }
        
        try {
            String email = principal.getAttribute("email");
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            PerfilAprendiz aprendiz = perfilAprendizRepository.findByUsuario(usuario);
            
            // Crear directorio si no existe
            Path uploadDir = Paths.get("src/main/resources/static/uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Generar nombre único
            String extension = getExtension(file.getOriginalFilename());
            String filename = "aprendiz_" + aprendiz.getId() + "_" + UUID.randomUUID() + "." + extension;
            Path filePath = uploadDir.resolve(filename);
            
            // Guardar archivo
            Files.copy(file.getInputStream(), filePath);
            
            // Actualizar perfil
            aprendiz.setFotoPerfil("/uploads/" + filename);
            perfilAprendizRepository.save(aprendiz);
            
            return "redirect:/estudiante/inicio";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/estudiante/inicio?error=fallo_carga";
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

}
