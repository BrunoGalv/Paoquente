package com.realvibedev.paoquente.model;

/**
 * Created by bruno on 20/10/18.
 */

public class ListCartoes {
    private String key, texto;

    public ListCartoes() {
    }


    public ListCartoes(String key, String texto) {
        this.key = key;
        this.texto = texto;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
