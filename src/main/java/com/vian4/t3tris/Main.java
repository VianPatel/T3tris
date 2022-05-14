package com.vian4.t3tris;

import com.jme3.system.AppSettings;

public class Main {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 600;//720;

    public static void main(String[] args) {
        T3tris app = new T3tris();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("T3tris");
        settings.put("Width", WIDTH);
        settings.put("Height", HEIGHT);
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();
    }
}
