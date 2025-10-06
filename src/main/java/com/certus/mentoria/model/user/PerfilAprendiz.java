package com.certus.mentoria.model.user;

import jakarta.persistence.*;


@Entity
public class PerfilAprendiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objetivo;
    private String nivelAcademico;
    @ManyToOne
    @JoinColumn(name = "carrera_id")
    private Carrera carrera;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    

    public PerfilAprendiz(Long id, String objetivo, String nivelAcademico, Carrera carrera, Usuario usuario) {
        this.id = id;
        this.objetivo = objetivo;
        this.nivelAcademico = nivelAcademico;
        this.carrera = carrera;
        this.usuario = usuario;
    }

    public PerfilAprendiz() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getNivelAcademico() {
        return nivelAcademico;
    }

    public void setNivelAcademico(String nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "PerfilAprendiz [id=" + id + ", objetivo=" + objetivo + ", nivelAcademico=" + nivelAcademico
                + ", carrera=" + carrera + ", usuario=" + usuario + "]";
    }

    
    
    
}
