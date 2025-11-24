package com.certus.mentoria.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.certus.mentoria.model.user.Rol;
import com.certus.mentoria.repository.RolRepository;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    public Rol guardarRol(Rol nombre) {
        return rolRepository.save(nombre);
    }

    public Optional<Rol> obtenerRolPorId(Long id) {
        return rolRepository.findById(id);
    }

    public void eliminarRol(Long id) {
        rolRepository.deleteById(id);
    }

    // ✅ Nuevo método
    public Rol buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}

