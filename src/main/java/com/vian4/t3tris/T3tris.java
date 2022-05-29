package com.vian4.t3tris;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioData.DataType;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.vian4.t3tris.gui.Title;

public class T3tris extends SimpleApplication {

    private AudioNode themeMusic;

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
        try {
            settings.setIcons(new BufferedImage[]{
                    ImageIO.read(T3tris.class.getResourceAsStream("/T3trisIcon256.png")),
                    ImageIO.read(T3tris.class.getResourceAsStream("/T3trisIcon128.png")),
                    ImageIO.read(T3tris.class.getResourceAsStream("/T3trisIcon32.png")),
                    ImageIO.read(T3tris.class.getResourceAsStream("/T3trisIcon16.png"))});
        } catch (IOException ignored) {}
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
        themeMusic = new AudioNode(assetManager, "AssetsNotCreatedByMe/Tetris.wav", DataType.Buffer);
        rootNode.attachChild(themeMusic);
        themeMusic.setLooping(true);
        themeMusic.play();

        GuiGlobals.initialize(this);
        flyCam.setEnabled(false);

        stateManager.attach(new Title());
    }

}