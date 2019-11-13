package com.realvibedev.paoquente.model;

/**
 * Created by bruno on 21/03/2018.
 */

public class PadariasTelaPrincipal {
    private String nomePadaria, imagemPadaria, ultimaFornada, key, local, imagemMapa;
    private double lat, longi;

    public PadariasTelaPrincipal() {
    }

    public PadariasTelaPrincipal(String nomePadaria, String imagemPadaria, String ultimaFornada, String key, String local, String imagemMapa, double lat, double longi) {
        this.nomePadaria = nomePadaria;
        this.imagemPadaria = imagemPadaria;
        this.ultimaFornada = ultimaFornada;
        this.key = key;
        this.local = local;
        this.imagemMapa = imagemMapa;
        this.lat = lat;
        this.longi = longi;
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

    public String getImagemMapa() {
        return imagemMapa;
    }

    public void setImagemMapa(String imagemMapa) {
        this.imagemMapa = imagemMapa;
    }

    public String getNomePadaria() {
        return nomePadaria;
    }

    public void setNomePadaria(String nomePadaria) {
        this.nomePadaria = nomePadaria;
    }

    public String getImagemPadaria() {
        return imagemPadaria;
    }

    public void setImagemPadaria(String imagemPadaria) {
        this.imagemPadaria = imagemPadaria;
    }

    public String getUltimaFornada() {
        return ultimaFornada;
    }

    public void setUltimaFornada(String ultimaFornada) {
        this.ultimaFornada = ultimaFornada;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
