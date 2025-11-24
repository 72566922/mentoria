package com.certus.mentoria.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.certus.mentoria.model.user.Especialidad;
import com.certus.mentoria.repository.EspecialidadRepository;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    // 🔹 Listar todas las especialidades
    public List<Especialidad> listarEspecialidades() {
        return especialidadRepository.findAll();
    }

    // 🔹 Guardar o actualizar
    public Especialidad guardarEspecialidad(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    // 🔹 Obtener por ID
    public Optional<Especialidad> obtenerPorId(Long id) {
        return especialidadRepository.findById(id);
    }

    // 🔹 Eliminar por ID
    public void eliminar(Long id) {
        especialidadRepository.deleteById(id);
    }
}
