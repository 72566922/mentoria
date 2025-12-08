package com.certus.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.certus.mentoria.model.sesion.Tema;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {
    List<Tema> findByEspecialidadId(Long especialidadId);
}
