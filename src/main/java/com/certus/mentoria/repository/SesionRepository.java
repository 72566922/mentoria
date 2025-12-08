package com.certus.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.certus.mentoria.model.sesion.Estado;
import com.certus.mentoria.model.sesion.Sesion;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    List<Sesion> findByAprendizUsuarioId(Long usuarioId);
    List<Sesion> findByMentorIdAndEstado(Long mentorId, Estado estado);
    List<Sesion> findByMentorIdAndEstadoIn(Long mentorId, List<Estado> estados);
    List<Sesion> findByMentorId(Long mentorId);


}
