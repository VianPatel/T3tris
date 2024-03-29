package com.vian4.t3tris.game;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.SpotLightShadowRenderer;

import com.vian4.t3tris.T3tris;
import com.vian4.t3tris.gamepiece.*;
import com.vian4.t3tris.gui.LoseState;
import com.vian4.t3tris.gui.WinState;

public class GameState extends BaseAppState {

    private T3tris t3tris;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;

    private GameBoard board;

    private Node planeNode;
    private Node center;

    private ChaseCamera chaseCam;

    private FilterPostProcessor fpp;
    private SpotLightShadowRenderer spotLightShadow;
    private AmbientLight ambientLighting;
    private SpotLight spotLight;

    private float timeWaited = 0.0f;

    @Override
    protected void initialize(Application app) {
        t3tris = (T3tris) app;

        this.rootNode = t3tris.getRootNode();
        this.assetManager = t3tris.getAssetManager();
        this.inputManager = t3tris.getInputManager();

        board = new GameBoard(t3tris.getAssetManager(), 20, 24, 10, 10);
        board.getBoxNode().setShadowMode(ShadowMode.CastAndReceive);
        
        
        initPlane();
        initCamera();
        initKeybinds();
        initLighting();
    }

    @Override
    protected void cleanup(Application app) {}

    @Override
    protected void onEnable() {
        inputManager.addListener(actionListener, "RotateX", "RotateZ", "Right", "Left", "Down", "Front", "Back");
        chaseCam.registerWithInput(inputManager);
        t3tris.getViewPort().addProcessor(spotLightShadow);
        t3tris.getViewPort().addProcessor(fpp);
        rootNode.attachChild(board.getBoxNode());
        rootNode.attachChild(planeNode);
        rootNode.attachChild(center);
        rootNode.addLight(ambientLighting);
        rootNode.addLight(spotLight);
    }

    @Override
    protected void onDisable() {
        inputManager.removeListener(actionListener);
        chaseCam.cleanupWithInput(inputManager);
        t3tris.getViewPort().removeProcessor(spotLightShadow);
        t3tris.getViewPort().removeProcessor(fpp);
        rootNode.detachChild(board.getBoxNode());
        rootNode.detachChild(planeNode);
        rootNode.detachChild(center);
        rootNode.removeLight(ambientLighting);
        rootNode.removeLight(spotLight);
    }

    @Override
    public void update(float tpf) {

        timeWaited += tpf;

        // printBoard(board);
        if (timeWaited >= 1) {
            timeWaited -= 1;
            if (!board.currentPieceSelected()) {
                board.setCurrentPiece(getRandomPiece());
            }

            if (board.currentPiece().moveDown()) {
                getStateManager().attach(new LoseState());
            }

            for (int y = 0; y < board.yLen(); y++) {
                if (board.isYPlainFilled(y)) {
                    getStateManager().attach(new WinState());
                    board.clearYPlain(y);
                }
            }

            for (int x = 0; x < board.xLen(); x++) {
                if (board.isXPlainFilled(x)) {
                    board.clearXPlain(x);
                }
            }

            for (int z = 0; z < board.zLen(); z++) {
                if (board.isZPlainFilled(z)) {
                    board.clearZPlain(z);
                }
            }
        }
    }

    private void initPlane() {
        planeNode = new Node();
        int planePadding = 2;
        Geometry plane = new Geometry("Plane", new Quad(2 * planePadding + board.xLen(), 2 * planePadding + board.zLen()));
        plane.rotate(-90 * FastMath.DEG_TO_RAD, -90 * FastMath.DEG_TO_RAD, 0);
        plane.setMaterial(board.getColor(ColorRGBA.LightGray));
        planeNode.attachChild(plane);
        planeNode.setLocalTranslation(-planePadding - 0.5f, 0.0f, -planePadding - 0.5f);

        planeNode.setShadowMode(ShadowMode.Receive);
    }

    private void initCamera() {
        center = new Node();
        center.setLocalTranslation(board.xLen() / 2, 0, board.zLen() / 2);

        chaseCam = new ChaseCamera(t3tris.getCamera(), center, inputManager);
        chaseCam.setSmoothMotion(true);
        chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(board.yLen() / 4));

        float minDistance = (float) Math.sqrt(board.xLen() * board.xLen() + board.yLen() * board.yLen() + board.zLen() * board.zLen());
        chaseCam.setMinDistance(minDistance * 1.1f);
        chaseCam.setDefaultDistance(minDistance * 1.5f);
        chaseCam.setMaxDistance(minDistance * 2.5f);
        chaseCam.setInvertVerticalAxis(true);
        chaseCam.setRotationSpeed(2.5f);
        chaseCam.setZoomSensitivity(4.0f);
    }

    private void initKeybinds() {
        inputManager.addMapping("RotateX",
                new KeyTrigger(KeyInput.KEY_E),
                new KeyTrigger(KeyInput.KEY_SLASH));
        inputManager.addMapping("RotateZ",
                new KeyTrigger(KeyInput.KEY_Q),
                new KeyTrigger(KeyInput.KEY_PERIOD));
        inputManager.addMapping("Right",
                new KeyTrigger(KeyInput.KEY_RIGHT),
                new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Left",
                new KeyTrigger(KeyInput.KEY_LEFT),
                new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Down",
                new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Front",
                new KeyTrigger(KeyInput.KEY_S),
                new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Back",
                new KeyTrigger(KeyInput.KEY_UP),
                new KeyTrigger(KeyInput.KEY_W));
    }

    private void initLighting() {
        ambientLighting = new AmbientLight();
        ambientLighting.setColor(ColorRGBA.White.mult(0.4f));

        spotLight = new SpotLight();
        spotLight.setSpotRange(1000f);
        spotLight.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD);
        spotLight.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD);
        spotLight.setColor(ColorRGBA.White.mult(1.6f));

        spotLight.setPosition(new Vector3f(-50, 250, 0));
        spotLight.setDirection((new Vector3f(0, 0, 0)).subtract(new Vector3f(0, 30, 2)).normalize());

        spotLightShadow = new SpotLightShadowRenderer(assetManager, 1024);
        spotLightShadow.setLight(spotLight);

        fpp = new FilterPostProcessor(assetManager);
        SSAOFilter ssaoFilter = new SSAOFilter(15f, 40f, 0.4f, 0.6f);
        fpp.addFilter(ssaoFilter);
    }


    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            char region = 'D';
            //Regions are:
            //   A
            // D   B
            //   C
            //seperated by y = x and y = -x
            Vector3f dir = t3tris.getCamera().getDirection().normalize();
            if (Math.abs(dir.getX()) <= dir.getZ()) {
                region = 'A';
            } else if (Math.abs(dir.getZ()) < dir.getX()) {
                region = 'B';
            } else if (-Math.abs(dir.getX()) >= dir.getZ()) {
                region = 'C';
            }

            if (name.equals("RotateX") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    if (region == 'C') {
                        board.currentPiece().rotateClockwiseX();
                    } else if (region == 'A') {
                        board.currentPiece().rotateCounterclockwiseX();
                    } else if (region == 'B') {
                        board.currentPiece().rotateClockwiseZ();
                    } else if (region == 'D') {
                        board.currentPiece().rotateCounterclockwiseZ();
                    }
                }
            } else if (name.equals("RotateZ") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    if (region == 'C') {
                        board.currentPiece().rotateClockwiseZ();
                    } else if (region == 'A') {
                        board.currentPiece().rotateCounterclockwiseZ();
                    } else if (region == 'B') {
                        board.currentPiece().rotateCounterclockwiseX();
                    } else if (region == 'D') {
                        board.currentPiece().rotateClockwiseX();
                    }
                }
            } else if (name.equals("Right") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    if (region == 'C') {
                        board.currentPiece().moveX(1);
                    } else if (region == 'A') {
                        board.currentPiece().moveX(-1);
                    } else if (region == 'B') {
                        board.currentPiece().moveZ(1);
                    } else if (region == 'D') {
                        board.currentPiece().moveZ(-1);
                    }
                }
            } else if (name.equals("Left") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    if (region == 'C') {
                        board.currentPiece().moveX(-1);
                    } else if (region == 'A') {
                        board.currentPiece().moveX(1);
                    } else if (region == 'B') {
                        board.currentPiece().moveZ(-1);
                    } else if (region == 'D') {
                        board.currentPiece().moveZ(1);
                    }
                }
            } else if (name.equals("Down") && !keyPressed) {
                while (board.currentPieceSelected()) {
                    board.currentPiece().moveDown();
                }
            } else if (name.equals("Front") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    if (region == 'C') {
                        board.currentPiece().moveZ(1);
                    } else if (region == 'A') {
                        board.currentPiece().moveZ(-1);
                    } else if (region == 'B') {
                        board.currentPiece().moveX(-1);
                    } else if (region == 'D') {
                        board.currentPiece().moveX(1);
                    }
                }
            } else if (name.equals("Back") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    if (region == 'C') {
                        board.currentPiece().moveZ(-1);
                    } else if (region == 'A') {
                        board.currentPiece().moveZ(1);
                    } else if (region == 'B') {
                        board.currentPiece().moveX(1);
                    } else if (region == 'D') {
                        board.currentPiece().moveX(-1);
                    }
                }
            }
        }
    };



    public GamePiece getRandomPiece() {
        int rand = (int) (Math.random() * 29) + 1;
        GamePiece piece = null;
        try {
            piece = (GamePiece) Class.forName("com.vian4.t3tris.gamepiece.Piece" + rand).getConstructor(GameBoard.class, ColorRGBA.class, Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(board, getRandomColor(), 4, 23, 4);
        } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | InstantiationException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Error creating class com.vian4.t3tris.gamepiece.Piece" + rand);
            e.printStackTrace();
            piece = new Piece1(board, getRandomColor(), 4, 23, 4);
        }
        return piece;
    }

    public ColorRGBA getRandomColor() {
        List<ColorRGBA> disallowedColors = Arrays.asList(new ColorRGBA[]{ColorRGBA.White, ColorRGBA.Pink, ColorRGBA.LightGray, ColorRGBA.Gray, ColorRGBA.DarkGray, ColorRGBA.Black, ColorRGBA.BlackNoAlpha});

        List<ColorRGBA> validColors = new ArrayList<>();

        for (Field color: ColorRGBA.class.getDeclaredFields()) {
            int modifiers = color.getModifiers();
            try {
                if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) &&
                        color.get(null) instanceof ColorRGBA &&
                        !disallowedColors.contains((ColorRGBA) (color.get(null)))) {
                    validColors.add((ColorRGBA) color.get(null));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {}
        }
        
        if (validColors.size() == 0) {
            return ColorRGBA.randomColor();
        }

        return validColors.get((int) (Math.random() * validColors.size()));
    }

    
}