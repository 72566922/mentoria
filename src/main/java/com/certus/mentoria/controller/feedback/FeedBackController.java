package com.certus.mentoria.controller.feedback;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.certus.mentoria.model.feedback.Feedback;
import com.certus.mentoria.model.user.Usuario;
import com.certus.mentoria.repository.FeedBackRepository;
import com.certus.mentoria.repository.PerfilMentorRepository;
import com.certus.mentoria.repository.UsuarioRepository;

@Controller
@RequestMapping("/mentor/feedback")
public class FeedBackController {

    private final FeedBackRepository feedBackRepository;
    private final PerfilMentorRepository perfilMentorRepository;

    private final UsuarioRepository usuarioRepository;

    public FeedBackController(FeedBackRepository feedBackRepository,
            PerfilMentorRepository perfilMentorRepository,
            UsuarioRepository usuarioRepository) {
        this.feedBackRepository = feedBackRepository;
        this.perfilMentorRepository = perfilMentorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String feedbackMentor(Model model, Principal principal) {

        // 1️⃣ Obtener email del usuario logeado
        String email = principal.getName();

        // 2️⃣ Buscar usuario
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3️⃣ Obtener perfil de mentor
        Long mentorId = perfilMentorRepository
                .findByUsuarioId(usuario.getId())
                .orElseThrow()
                .getId();

        // 4️⃣ Obtener feedback del mentor logeado
        List<Feedback> feedbacks = feedBackRepository.findBySesionMentorId(mentorId);

        model.addAttribute("feedbacks", feedbacks);

        return "mentores/feedback";
    }

}
