package com.certus.mentoria.service;

import java.time.LocalDateTime;

import com.certus.mentoria.model.sesion.Sesion;
import com.certus.mentoria.model.user.PerfilAprendiz;
import com.certus.mentoria.model.user.PerfilMentor;

public interface SesionService {
    Sesion agendarSesion(PerfilMentor mentor, PerfilAprendiz aprendiz, LocalDateTime fechaHora);
}
