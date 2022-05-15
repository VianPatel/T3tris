package com.vian4.t3tris;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.vian4.t3tris.gui.Tutorial;

public class T3tris extends SimpleApplication {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 600;// 720;

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

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        flyCam.setEnabled(false);

        setPauseOnLostFocus(true);
        setDisplayStatView(false);

        stateManager.attach(new Tutorial());
    }

    /*@Override
    public void simpleUpdate(float tpf) {}*/

}