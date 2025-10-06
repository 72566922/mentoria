package com.certus.mentoria.model.feedback;


import com.certus.mentoria.model.user.PerfilAprendiz;
import com.certus.mentoria.model.user.PerfilMentor;

import jakarta.persistence.*;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int calificacion; // 1 a 5
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private PerfilMentor mentor;

    @ManyToOne
    @JoinColumn(name = "aprendiz_id")
    private PerfilAprendiz aprendiz;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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

    public Feedback() {
    }

    public Feedback(Long id, int calificacion, String comentario, PerfilMentor mentor, PerfilAprendiz aprendiz) {
        this.id = id;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.mentor = mentor;
        this.aprendiz = aprendiz;
    }

    @Override
    public String toString() {
        return "Feedback [id=" + id + ", calificacion=" + calificacion + ", comentario=" + comentario + ", mentor="
                + mentor + ", aprendiz=" + aprendiz + "]";
    }

    
}


