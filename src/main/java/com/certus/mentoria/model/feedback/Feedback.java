package com.certus.mentoria.model.feedback;


import com.certus.mentoria.model.sesion.Sesion;
import com.certus.mentoria.model.user.Usuario;

import jakarta.persistence.*;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int calificacion; // 1 a 5
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Sesion sesion;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;

    public Feedback() {
    }

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

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Feedback(Long id, int calificacion, String comentario, Sesion sesion, Usuario usuario) {
        this.id = id;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.sesion = sesion;
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Feedback [id=" + id + ", calificacion=" + calificacion + ", comentario=" + comentario + ", sesion="
                + sesion + ", usuario=" + usuario + "]";
    }

    
}


