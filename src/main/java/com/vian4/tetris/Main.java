package com.vian4.tetris;

import com.jme3.system.AppSettings;

public class Main {

    protected static final int WIDTH = 1280;
    protected static final int HEIGHT = 720;

    private static ThreeDTetris app;

    public static void main(String[] args) {
        app = new ThreeDTetris();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("3D Tetris");
        settings.put("Width", WIDTH);
        settings.put("Height", HEIGHT);
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();
    }
}
