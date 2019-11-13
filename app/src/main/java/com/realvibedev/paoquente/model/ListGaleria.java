package com.realvibedev.paoquente.model;

/**
 * Created by bruno on 24/05/2018.
 */

public class ListGaleria {

    private String key, imagem;

    public ListGaleria() {
    }

    public ListGaleria(String key, String imagem) {
        this.key = key;
        this.imagem = imagem;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
