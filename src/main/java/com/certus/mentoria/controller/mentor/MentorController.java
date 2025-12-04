package com.certus.mentoria.controller.mentor;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.certus.mentoria.model.user.Usuario;
import com.certus.mentoria.model.feedback.Feedback;
import com.certus.mentoria.model.sesion.Estado;
import com.certus.mentoria.model.sesion.Sesion;
import com.certus.mentoria.model.user.PerfilMentor;
import com.certus.mentoria.repository.UsuarioRepository;
import com.certus.mentoria.repository.FeedBackRepository;
import com.certus.mentoria.repository.PerfilMentorRepository;
import com.certus.mentoria.repository.SesionRepository;

@Controller
@RequestMapping("/mentor")
public class MentorController {

        private final UsuarioRepository usuarioRepository;
        private final PerfilMentorRepository perfilMentorRepository;
        private final SesionRepository sesionRepository;
        private final FeedBackRepository feedBackRepository;

        public MentorController(
                        UsuarioRepository usuarioRepository,
                        PerfilMentorRepository perfilMentorRepository,
                        SesionRepository sesionRepository,
                        FeedBackRepository feedBackRepository) {

                this.usuarioRepository = usuarioRepository;
                this.perfilMentorRepository = perfilMentorRepository;
                this.sesionRepository = sesionRepository;
                this.feedBackRepository = feedBackRepository;
        }

        @GetMapping
        public String mentor(Model model, Principal principal) {

                // 1️⃣ Obtener email del usuario autenticado
                String email = principal.getName();

                // 2️⃣ Buscar usuario
                Usuario usuario = usuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // 3️⃣ Perfil del mentor
                PerfilMentor perfil = perfilMentorRepository
                                .findByUsuarioId(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Perfil de mentor no encontrado"));

                Long mentorId = perfil.getId();

                // 4️⃣ Sesiones por estado
                List<Sesion> sesionesPendientes = sesionRepository.findByMentorIdAndEstado(mentorId, Estado.PENDIENTE);

                List<Sesion> sesionesConfirmadas = sesionRepository.findByMentorIdAndEstado(mentorId,
                                Estado.CONFIRMADA);

                // 5️⃣ Feedback del mentor
                List<Feedback> feedbacks = feedBackRepository.findBySesionMentorId(mentorId);

                // 6️⃣ Enviar a la vista
                model.addAttribute("usuario", usuario);
                model.addAttribute("perfil", perfil);
                model.addAttribute("sesionesConfirmadas", sesionesConfirmadas);
                model.addAttribute("sesionesPendientes", sesionesPendientes);
                model.addAttribute("feedbacks", feedbacks);

                return "mentores/mentor";
        }

}
