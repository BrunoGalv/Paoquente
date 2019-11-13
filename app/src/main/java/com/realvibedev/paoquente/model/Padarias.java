package com.realvibedev.paoquente.model;

/**
 * Created by bruno on 19/03/2018.
 */

public class Padarias {
    private String nomePadaria, ultimaFornada, imagemPadaria, key, ref, avaliacao, numAvaliacoes;
    private double lat, longi;
    private float km;

    public Padarias(){

    }

    public Padarias(String nomePadaria, String ultimaFornada, String imagemPadaria, String key, String ref, String avaliacao, String numAvaliacoes, double lat, double longi, float km) {
        this.nomePadaria = nomePadaria;
        this.ultimaFornada = ultimaFornada;
        this.imagemPadaria = imagemPadaria;
        this.key = key;
        this.ref = ref;
        this.avaliacao = avaliacao;
        this.numAvaliacoes = numAvaliacoes;
        this.lat = lat;
        this.longi = longi;
        this.km = km;
    }

    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getNumAvaliacoes() {
        return numAvaliacoes;
    }

    public void setNumAvaliacoes(String numAvaliacoes) {
        this.numAvaliacoes = numAvaliacoes;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public String getNomePadaria() {
        return nomePadaria;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setNomePadaria(String nomePadaria) {
        this.nomePadaria = nomePadaria;
    }

    public String getUltimaFornada() {
        return ultimaFornada;
    }

    public void setUltimaFornada(String ultimaFornada) {
        this.ultimaFornada = ultimaFornada;
    }

    public String getImagemPadaria() {
        return imagemPadaria;
    }

    public void setImagemPadaria(String imagemPadaria) {
        this.imagemPadaria = imagemPadaria;
    }
}
