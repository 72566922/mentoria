package com.certus.mentoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.certus.mentoria.model.user.Carrera;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    
}
