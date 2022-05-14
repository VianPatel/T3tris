package com.vian4.t3tris;

import com.jme3.system.AppSettings;

public class Main {

    protected static final int WIDTH = 1280;
    protected static final int HEIGHT = 600;//720;

    private static T3tris app;

    public static void main(String[] args) {
        app = new T3tris();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("T3tris");
        settings.put("Width", WIDTH);
        settings.put("Height", HEIGHT);
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();
    }
}
