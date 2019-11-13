package com.realvibedev.paoquente.model;

/**
 * Created by bruno on 20/10/18.
 */

public class ListTelefone {
    private String key, numeroTelefone;

    public ListTelefone() {
    }

    public ListTelefone(String key, String numeroTelefone) {
        this.key = key;
        this.numeroTelefone = numeroTelefone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }
}
