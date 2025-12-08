package com.certus.mentoria.model.user;

import jakarta.persistence.*;

@Entity

public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // ADMIN, MENTOR, APRENDIZ

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Rol() {
    }

    @Override
    public String toString() {
        return "Rol [id=" + id + ", nombre=" + nombre + "]";
    }

    public Rol(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Rol(String nombre) {
        this.nombre = nombre;
    }

}
