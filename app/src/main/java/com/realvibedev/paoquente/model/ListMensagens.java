package com.realvibedev.paoquente.model;

/**
 * Created by Meu computador on 13/03/2019.
 */

public class ListMensagens {
    private String key, nome, ultMsg;
    private Double keyChat;
    private Double ordem;

    public ListMensagens() {
    }

    public ListMensagens(String key, String nome, String ultMsg, Double keyChat, Double ordem) {
        this.key = key;
        this.nome = nome;
        this.ultMsg = ultMsg;
        this.keyChat = keyChat;
        this.ordem = ordem;
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

    public String getUltMsg() {
        return ultMsg;
    }

    public void setUltMsg(String ultMsg) {
        this.ultMsg = ultMsg;
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
}
