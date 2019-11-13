package com.realvibedev.paoquente.model;

import android.net.Uri;

/**
 * Created by bruno on 29/03/2018.
 */

public class Notification {
    public String body;
    public String title;
    public Uri sound;
    public String color;

    public Notification(String body, String title, Uri sound, String color) {
        this.body = body;
        this.title = title;
        this.sound = sound;
        this.color = color;
    }

}
