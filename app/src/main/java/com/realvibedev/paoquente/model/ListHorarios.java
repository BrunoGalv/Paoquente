package com.realvibedev.paoquente.model;

/**
 * Created by bruno on 20/10/18.
 */

public class ListHorarios {

    private String key, horario;

    public ListHorarios() {
    }

    public ListHorarios(String key, String horario) {
        this.key = key;
        this.horario = horario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
