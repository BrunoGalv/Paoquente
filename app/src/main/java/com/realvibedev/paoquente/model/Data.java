package com.realvibedev.paoquente.model;

/**
 * Created by bruno on 05/04/2018.
 */

public class Data {
    public String titulo, imagem, texto, chId, aux, userId;

    public Data() {
    }

    public Data(String titulo, String texto, String imagem, String chId, String aux, String userId) {
        this.titulo = titulo;
        this.imagem = imagem;
        this.texto = texto;
        this.chId = chId;
        this.aux = aux;
        this.userId = userId;
    }
}
