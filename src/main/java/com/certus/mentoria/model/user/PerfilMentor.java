package com.certus.mentoria.model.user;

import jakarta.persistence.*;


@Entity
public class PerfilMentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad; // relación hacia la entidad

    private String disponibilidad;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PerfilMentor(Long id, Especialidad especialidad, String disponibilidad, Usuario usuario) {
        this.id = id;
        this.especialidad = especialidad;
        this.disponibilidad = disponibilidad;
        this.usuario = usuario;
    }

    public PerfilMentor() {
    }

    @Override
    public String toString() {
        return "PerfilMentor [id=" + id + ", especialidad=" + especialidad + ", disponibilidad=" + disponibilidad
                + ", usuario=" + usuario + "]";
    }

    


}

