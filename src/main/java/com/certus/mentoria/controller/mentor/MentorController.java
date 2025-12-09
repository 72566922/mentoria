package com.certus.mentoria.controller.mentor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.certus.mentoria.model.user.Usuario;
import com.certus.mentoria.model.feedback.Feedback;
import com.certus.mentoria.model.sesion.Estado;
import com.certus.mentoria.model.sesion.Sesion;
import com.certus.mentoria.model.user.Especialidad;
import com.certus.mentoria.model.user.PerfilMentor;
import com.certus.mentoria.repository.UsuarioRepository;
import com.certus.mentoria.service.GoogleCalendarService;
import com.certus.mentoria.service.SesionService;
import com.certus.mentoria.repository.EspecialidadRepository;
import com.certus.mentoria.repository.FeedBackRepository;
import com.certus.mentoria.repository.PerfilMentorRepository;
import com.certus.mentoria.repository.SesionRepository;

@Controller
@RequestMapping("/mentor")
public class MentorController {

        private final SesionService sesionService;
        private final UsuarioRepository usuarioRepository;
        private final PerfilMentorRepository perfilMentorRepository;
        private final SesionRepository sesionRepository;
        private final FeedBackRepository feedBackRepository;

        private final EspecialidadRepository especialidadRepository;

        private final GoogleCalendarService googleCalendarService;
        private final OAuth2AuthorizedClientService authorizedClientService;

        public MentorController(SesionService sesionService,
                        UsuarioRepository usuarioRepository,
                        PerfilMentorRepository perfilMentorRepository,
                        SesionRepository sesionRepository,
                        FeedBackRepository feedBackRepository,
                        EspecialidadRepository especialidadRepository,
                        GoogleCalendarService googleCalendarService,
                        OAuth2AuthorizedClientService authorizedClientService) {
                this.sesionService = sesionService;
                this.usuarioRepository = usuarioRepository;
                this.perfilMentorRepository = perfilMentorRepository;
                this.sesionRepository = sesionRepository;
                this.feedBackRepository = feedBackRepository;
                this.especialidadRepository = especialidadRepository;
                this.googleCalendarService = googleCalendarService;
                this.authorizedClientService = authorizedClientService;
        }

        @GetMapping
        public String mentor(Model model, @AuthenticationPrincipal OAuth2User principal) {

                // 1️⃣ Obtener email del usuario autenticado
                String email = principal.getAttribute("email");

                // 2️⃣ Buscar usuario
                Usuario usuario = usuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // 3️⃣ Perfil del mentor
                PerfilMentor perfil = perfilMentorRepository
                                .findByUsuarioId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Perfil de mentor no encontrado"));

                Long mentorId = perfil.getId();

                // 4️⃣ Sesiones por estado
                List<Sesion> sesiones = sesionRepository.findByMentorId(mentorId);

                Map<String, List<Sesion>> sesionesPorEstado = new HashMap<>();
                for (Estado estado : Estado.values()) {
                        sesionesPorEstado.put(estado.name().toLowerCase(),
                                        sesiones.stream().filter(s -> s.getEstado() == estado).toList());
                }

                // 5️⃣ Feedback del mentor
                List<Feedback> feedbacks = feedBackRepository.findBySesionMentorId(mentorId);
                List<Especialidad> especialidades = especialidadRepository.findAll();
                model.addAttribute("especialidades", especialidades);

                // 6️⃣ Enviar a la vista
                model.addAttribute("usuario", usuario);
                model.addAttribute("perfil", perfil);
                model.addAttribute("sesionesPorEstado", sesionesPorEstado);
                model.addAttribute("feedbacks", feedbacks);

                return "mentores/mentor";
        }

        @GetMapping("/mentor/dashboard")
        public String mostrarDashboard(Model model, @AuthenticationPrincipal OAuth2User principal) {

                // Obtener email y perfil del mentor
                String email = principal.getAttribute("email");
                Usuario usuario = usuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                PerfilMentor perfil = perfilMentorRepository.findByUsuarioId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Perfil de mentor no encontrado"));
                Long mentorId = perfil.getId();

                // Obtener todas las sesiones del mentor
                List<Sesion> sesiones = sesionService.obtenerSesionesMentor(mentorId);

                Map<String, List<Sesion>> sesionesPorEstado = new HashMap<>();
                for (Estado estado : Estado.values()) {
                        sesionesPorEstado.put(estado.name().toLowerCase(),
                                        sesiones.stream().filter(s -> s.getEstado() == estado).toList());
                }
                model.addAttribute("sesionesPorEstado", sesionesPorEstado);

                // Pasar enum al modelo
                model.addAttribute("estados", Estado.values());

                return "mentor/dashboard";
        }

        @PostMapping("/cambiarEstado")
        public String cambiarEstado(@RequestParam Long sesionId,
                        @RequestParam Estado nuevoEstado,
                        @AuthenticationPrincipal DefaultOidcUser principal) throws Exception {

                sesionService.actualizarEstado(sesionId, nuevoEstado);

                if (nuevoEstado == Estado.CONFIRMADA) {
                        googleCalendarService.crearEventoSesion(sesionRepository.findById(sesionId).get(), principal);
                }

                return "redirect:/mentor";
        }

        @PostMapping("/subirFoto")
        public String subirFoto(@RequestParam("foto") MultipartFile foto,
                        @AuthenticationPrincipal OAuth2User principal) {
                try {
                        String email = principal.getAttribute("email");
                        Usuario usuario = usuarioRepository.findByEmail(email)
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        PerfilMentor perfil = perfilMentorRepository.findByUsuarioId(usuario.getId())
                                        .orElseThrow(() -> new RuntimeException("Perfil de mentor no encontrado"));

                        // Validar que sea imagen
                        if (!foto.getContentType().startsWith("image/")) {
                                throw new RuntimeException("El archivo debe ser una imagen");
                        }

                        // Crear nombre único para la imagen
                        String nombreArchivo = "mentor_" + perfil.getId() + "_" + UUID.randomUUID() +
                                        getExtension(foto.getOriginalFilename());

                        // Guardar en carpeta uploads
                        Path rutaGuardado = Paths.get("src/main/resources/static/uploads/" + nombreArchivo);
                        Files.createDirectories(rutaGuardado.getParent());
                        Files.write(rutaGuardado, foto.getBytes());

                        // Actualizar perfil con la ruta de la imagen
                        perfil.setFotoPerfil("/uploads/" + nombreArchivo);
                        perfilMentorRepository.save(perfil);

                } catch (IOException e) {
                        e.printStackTrace();
                }

                return "redirect:/mentor";
        }

        @PostMapping("/editarPerfil")
        public String editarPerfil(
                        @RequestParam String disponibilidad,
                        @RequestParam Long especialidadId,
                        @AuthenticationPrincipal OAuth2User principal) {

                String email = principal.getAttribute("email");
                Usuario usuario = usuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                PerfilMentor perfil = perfilMentorRepository.findByUsuarioId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Perfil de mentor no encontrado"));

                // Actualizar disponibilidad
                perfil.setDisponibilidad(disponibilidad);

                // Actualizar especialidad
                especialidadRepository.findById(especialidadId)
                                .ifPresent(perfil::setEspecialidad);

                perfilMentorRepository.save(perfil);

                return "redirect:/mentor";
        }

        private String getExtension(String filename) {
                return filename.substring(filename.lastIndexOf("."));
        }

        @PostMapping("/confirmarSesion")
        public String confirmarSesion(
                        @RequestParam Long sesionId,
                        @AuthenticationPrincipal OAuth2User principal) throws Exception {

                // 1️⃣ Actualizar estado de la sesión
                Sesion sesion = sesionRepository.findById(sesionId)
                                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
                sesion.setEstado(Estado.CONFIRMADA);
                sesionRepository.save(sesion);

                // 2️⃣ Aquí puedes agregar la integración con Google Calendar
                // Por ejemplo, usando un servicio GoogleCalendarService que hayas creado
                // googleCalendarService.crearEvento(sesion, principal);

                return "redirect:/mentor"; // O "/mentor/dashboard"
        }

}