package com.pm.ramilton.pontoeletronico.Model;

public class Viagem {

    private String objectId;
    private String data;
    private String hora;
    private String fkOnjectIdUser;
    private String JustificativaInicioJornada;

    public String getJustificativaInicioJornada() {
        return JustificativaInicioJornada;
    }

    public void setJustificativaInicioJornada(String justificativaInicioJornada) {
        JustificativaInicioJornada = justificativaInicioJornada;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFkOnjectIdUser() {
        return fkOnjectIdUser;
    }

    public void setFkOnjectIdUser(String fkOnjectIdUser) {
        this.fkOnjectIdUser = fkOnjectIdUser;
    }
}
