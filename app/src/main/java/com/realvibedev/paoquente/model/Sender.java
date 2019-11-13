package com.realvibedev.paoquente.model;

/**
 * Created by bruno on 29/03/2018.
 */

public class Sender {
    public Notification notification;
    public String to;
    public Data data;

    public Sender() {
    }

    public Sender(String to, Data data) {
        this.notification = notification;
        this.to = to;
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
