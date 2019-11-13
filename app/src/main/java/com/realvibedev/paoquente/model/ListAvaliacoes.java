package com.realvibedev.paoquente.model;

/**
 * Created by bruno on 22/05/2018.
 */

public class ListAvaliacoes {
    private String nome, avaliacao, texto, key, imagem;

    public ListAvaliacoes() {
    }

    public ListAvaliacoes(String nome, String avaliacao, String texto, String key, String imagem) {
        this.nome = nome;
        this.avaliacao = avaliacao;
        this.texto = texto;
        this.key = key;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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
