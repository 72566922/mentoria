package com.certus.mentoria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.certus.mentoria.model.user.Carrera;
import com.certus.mentoria.repository.CarreraRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CarreraService {

    @Autowired
    private CarreraRepository carreraRepository;

    public List<Carrera> listarCarreras() {
        return carreraRepository.findAll();
    }

    public Carrera guardarCarrera(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    public Optional<Carrera> obtenerCarreraPorId(Long id) {
        return carreraRepository.findById(id);
    }

    public void eliminarCarrera(Long id) {
        carreraRepository.deleteById(id);
    }
}
