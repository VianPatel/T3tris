package com.vian4.t3tris;

import java.awt.image.BufferedImage;
import java.io.IOException;

import java.awt.GraphicsDevice;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
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

        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] modes = device.getDisplayModes();
        settings.setResolution(modes[0].getWidth(), modes[0].getHeight());
        settings.setFrequency(modes[0].getRefreshRate());
        settings.setBitsPerPixel(modes[0].getBitDepth());
        settings.setFullscreen(device.isFullScreenSupported());
        
        settings.setVSync(true);
        settings.setGammaCorrection(true);
        settings.setSettingsDialogImage("/T3trisLogo256.png");
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
        themeMusic = new AudioNode(assetManager, "copyrightedAssets/Tetris.wav", DataType.Buffer);
        rootNode.attachChild(themeMusic);
        themeMusic.setPositional(false);
        themeMusic.setLooping(true);
        themeMusic.play();

        GuiGlobals.initialize(this);
        flyCam.setEnabled(false);

        stateManager.attach(new Title());
    }

}
