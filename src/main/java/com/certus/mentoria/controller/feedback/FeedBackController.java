package com.certus.mentoria.controller.feedback;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.certus.mentoria.model.feedback.Feedback;
import com.certus.mentoria.repository.FeedBackRepository;
import com.certus.mentoria.repository.PerfilMentorRepository;

@Controller
@RequestMapping("/mentor/feedback")
public class FeedBackController {

    private final FeedBackRepository feedBackRepository;
    private final PerfilMentorRepository perfilMentorRepository;

    public FeedBackController(FeedBackRepository feedBackRepository,
                              PerfilMentorRepository perfilMentorRepository) {
        this.feedBackRepository = feedBackRepository;
        this.perfilMentorRepository = perfilMentorRepository;
    }

    @GetMapping
    public String feedbackMentor(Model model) {

        Long idUsuario = 2L; // temporal

        Long mentorId = perfilMentorRepository
                .findByUsuarioId(idUsuario)
                .orElseThrow()
                .getId();

        List<Feedback> feedbacks = feedBackRepository.findBySesionMentorId(mentorId);

        model.addAttribute("feedbacks", feedbacks);

        return "mentores/feedback"; // tu vista Thymeleaf
    }
}
