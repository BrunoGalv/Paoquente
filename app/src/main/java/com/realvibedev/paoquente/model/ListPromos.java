package com.realvibedev.paoquente.model;

/**
 * Created by Meu computador on 28/02/2019.
 */

public class ListPromos {
    private String nome, descricao, imagem, preco, key, nomePadaria, keyPd, txtPromo, txtPromo2;
    private double lat, longi;
    private float km;


    public ListPromos(String nome, String descricao, String imagem, String preco, String key, String nomePadaria, String keyPd, String txtPromo, String txtPromo2, double lat, double longi, float km) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
        this.preco = preco;
        this.key = key;
        this.nomePadaria = nomePadaria;
        this.keyPd = keyPd;
        this.txtPromo = txtPromo;
        this.txtPromo2 = txtPromo2;
        this.lat = lat;
        this.longi = longi;
        this.km = km;
    }

    public ListPromos() {
    }

    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public String getTxtPromo() {
        return txtPromo;
    }

    public void setTxtPromo(String txtPromo) {
        this.txtPromo = txtPromo;
    }

    public String getTxtPromo2() {
        return txtPromo2;
    }

    public void setTxtPromo2(String txtPromo2) {
        this.txtPromo2 = txtPromo2;
    }

    public String getKeyPd() {
        return keyPd;
    }

    public void setKeyPd(String keyPd) {
        this.keyPd = keyPd;
    }

    public String getNomePadaria() {
        return nomePadaria;
    }

    public void setNomePadaria(String nomePadaria) {
        this.nomePadaria = nomePadaria;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
