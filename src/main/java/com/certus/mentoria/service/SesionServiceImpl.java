package com.certus.mentoria.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.certus.mentoria.model.sesion.Estado;
import com.certus.mentoria.model.sesion.Sesion;
import com.certus.mentoria.model.user.PerfilAprendiz;
import com.certus.mentoria.model.user.PerfilMentor;
import com.certus.mentoria.repository.SesionRepository;

@Service
public class SesionServiceImpl implements SesionService {

    private final SesionRepository sesionRepository;

    public SesionServiceImpl(SesionRepository sesionRepository) {
        this.sesionRepository = sesionRepository;
    }

    @Override
    public Sesion agendarSesion(PerfilMentor mentor, PerfilAprendiz aprendiz, LocalDateTime fechaHora) {
        Sesion sesion = new Sesion();
        sesion.setMentor(mentor);
        sesion.setAprendiz(aprendiz);
        sesion.setFechaHora(fechaHora);
        sesion.setEstado(Estado.PENDIENTE);
        return sesionRepository.save(sesion);
    }

    @Override
    public List<Sesion> obtenerSesionesMentor(Long mentorId) {
        return sesionRepository.findByMentorId(mentorId);
    }

    @Override
    public void actualizarEstado(Long sesionId, Estado nuevoEstado) {
        Sesion sesion = sesionRepository.findById(sesionId)
            .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setEstado(nuevoEstado);
        sesionRepository.save(sesion);
    }
}
