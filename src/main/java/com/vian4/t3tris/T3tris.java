package com.vian4.t3tris;

import java.awt.GraphicsEnvironment;
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.vian4.t3tris.gui.Tutorial;

public class T3tris extends SimpleApplication {

    public static void main(String[] args) {
        T3tris app = new T3tris();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("T3tris");
        java.awt.DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        settings.put("Width", displayMode.getWidth());
        settings.put("Height", displayMode.getHeight());
        //settings.setFullscreen(true);
        settings.setVSync(true);
        settings.setGammaCorrection(true);
        settings.setSettingsDialogImage("T3trisIcon.png");
        app.setShowSettings(false);
        app.setDisplayStatView(false);
        app.setDisplayFps(false);
        app.setPauseOnLostFocus(true);
        app.setSettings(settings);
        app.start();
    }

    public int getWidth() {
        return settings.getWidth();
    }

    public int getHeight() {
        return settings.getHeight();
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        flyCam.setEnabled(false);

        stateManager.attach(new Tutorial());
    }

}