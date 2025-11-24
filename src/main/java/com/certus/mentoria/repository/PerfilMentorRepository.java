package com.certus.mentoria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.certus.mentoria.model.user.PerfilMentor;

@Repository
public interface PerfilMentorRepository extends JpaRepository<PerfilMentor, Long> {

    // 🔹 Obtener todos los mentores (solo los usuarios con rol 'MENTOR')
    @Query(value = """
        SELECT p.*
        FROM perfil_mentor p
        JOIN usuario u ON p.usuario_id = u.id
        JOIN usuario_rol ur ON u.id = ur.usuario_id
        JOIN rol r ON ur.rol_id = r.id
        WHERE r.nombre = 'MENTOR'
        """, nativeQuery = true)
    List<PerfilMentor> findAllMentores();

    Optional<PerfilMentor> findByUsuarioId(Long usuarioId);

}
