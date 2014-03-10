package com.gestionmensjaes.app.data;

/**
 * Created by alejandro on 10/02/14.
 */
public class Mensajes {
    private String id;
    private String asunto;
    private String contenido;
    private String remitente;
    private String grupo;

    public Mensajes(String id, String asunto, String contenido, String remitente, String grupo) {
        this.id = id;
        this.asunto = asunto;
        this.contenido = contenido;
        this.remitente = remitente;
        this.grupo = grupo;
    }

    public String getAsunto() {

        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return asunto;
    }
}
