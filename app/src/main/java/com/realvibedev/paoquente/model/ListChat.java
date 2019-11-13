package com.realvibedev.paoquente.model;

public class ListChat {

    private String msg, key;
    private Double aux;

    public ListChat() {
    }

    public ListChat(String msg, String key, Double aux) {
        this.msg = msg;
        this.key = key;
        this.aux = aux;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getAux() {
        return aux;
    }

    public void setAux(Double aux) {
        this.aux = aux;
    }
}
