package com.realvibedev.paoquente.model;

public class ListMensagensCliente {

    private String key, imagem, nome, ultMsg, salOuAut;
    private Double keyChat;
    private Double ordem;

    public ListMensagensCliente() {
    }

    public ListMensagensCliente(String key, String imagem, String nome, String ultMsg, String salOuAut, Double keyChat, Double ordem) {
        this.key = key;
        this.imagem = imagem;
        this.nome = nome;
        this.ultMsg = ultMsg;
        this.salOuAut = salOuAut;
        this.keyChat = keyChat;
        this.ordem = ordem;
    }


    public String getSalOuAut() {
        return salOuAut;
    }

    public void setSalOuAut(String salOuAut) {
        this.salOuAut = salOuAut;
    }

    public Double getKeyChat() {
        return keyChat;
    }

    public void setKeyChat(Double keyChat) {
        this.keyChat = keyChat;
    }

    public Double getOrdem() {
        return ordem;
    }

    public void setOrdem(Double ordem) {
        this.ordem = ordem;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUltMsg() {
        return ultMsg;
    }

    public void setUltMsg(String ultMsg) {
        this.ultMsg = ultMsg;
    }
}
