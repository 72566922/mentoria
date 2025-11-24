package com.certus.mentoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.certus.mentoria.model.user.PerfilAprendiz;
import com.certus.mentoria.model.user.Usuario;

@Repository
public interface PerfilAprendizRepository extends JpaRepository<PerfilAprendiz, Long> {
    PerfilAprendiz findByUsuario(Usuario usuario);
}
