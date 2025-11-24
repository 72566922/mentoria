package com.certus.mentoria.model.sesion;

import java.time.LocalDateTime;

import com.certus.mentoria.model.user.PerfilAprendiz;
import com.certus.mentoria.model.user.PerfilMentor;

import jakarta.persistence.*;

@Entity
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    private Estado estado; // ← ahora usa el enum correctamente

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private PerfilMentor mentor;

    @ManyToOne
    @JoinColumn(name = "aprendiz_id")
    private PerfilAprendiz aprendiz;

    @Column(nullable = false)
    private String tema;

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public PerfilMentor getMentor() {
        return mentor;
    }

    public void setMentor(PerfilMentor mentor) {
        this.mentor = mentor;
    }

    public PerfilAprendiz getAprendiz() {
        return aprendiz;
    }

    public void setAprendiz(PerfilAprendiz aprendiz) {
        this.aprendiz = aprendiz;
    }

    public Sesion() {
    }

    public Sesion(Long id, LocalDateTime fechaHora, Estado estado, PerfilMentor mentor, PerfilAprendiz aprendiz,
            String tema) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.mentor = mentor;
        this.aprendiz = aprendiz;
        this.tema = tema;
    }

    @Override
    public String toString() {
        return "Sesion [id=" + id + ", fechaHora=" + fechaHora + ", estado=" + estado + ", mentor=" + mentor
                + ", aprendiz=" + aprendiz + ", tema=" + tema + "]";
    }

    

}
