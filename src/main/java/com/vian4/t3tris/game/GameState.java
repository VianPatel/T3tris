package com.vian4.t3tris.game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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

public class GameState extends BaseAppState {

    private T3tris t3tris;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;

    private GameBoard board;

    private Node planeNode;
    private Node center;

    private float timeWaited = 0.0f;

    @Override
    protected void initialize(Application app) {
        t3tris = (T3tris) app;

        this.rootNode = t3tris.getRootNode();
        this.assetManager = t3tris.getAssetManager();
        this.inputManager = t3tris.getInputManager();
        //this.physics = this.stateManager.getState(BulletAppState.class);

        board = new GameBoard(t3tris.getAssetManager(), 20, 24, 10, 10);
        rootNode.attachChild(board.getBoxNode());
        board.getBoxNode().setShadowMode(ShadowMode.CastAndReceive);
        
        
        initPlane();
        initCamera();
        initKeybinds();
        initLighting();
    }

    @Override
    protected void cleanup(Application app) {
        rootNode.detachChild(board.getBoxNode());
        rootNode.detachChild(planeNode);
        rootNode.detachChild(center);
    }

    @Override
    protected void onEnable() {
        inputManager.addListener(actionListener, "RotateX", "RotateZ", "Right", "Left", "Down", "Front", "Back");
    }

    @Override
    protected void onDisable() {
        inputManager.removeListener(actionListener);
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
                System.out.println("Game Over!\nThanks for playing.");
                //stop();
            }

            // TODO plane clearing and or checking methods are not working
            for (int y = 0; y < board.yLen(); y++) {
                if (board.isYPlainFilled(y)) {
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
            // board.currentPiece().rotate();
        }
    }

    private void initPlane() {
        planeNode = new Node();
        int planePadding = 2;
        Geometry plane = new Geometry("Plane",
                new Quad(2 * planePadding + board.xLen(), 2 * planePadding + board.zLen()));
        plane.rotate(-90 * FastMath.DEG_TO_RAD, -90 * FastMath.DEG_TO_RAD, 0);
        plane.setMaterial(board.getColor(ColorRGBA.LightGray));
        planeNode.attachChild(plane);
        planeNode.setLocalTranslation(-planePadding - 0.5f, 0.0f, -planePadding - 0.5f);

        rootNode.attachChild(planeNode);
        planeNode.setShadowMode(ShadowMode.Receive);
    }

    private void initCamera() {
        center = new Node();
        rootNode.attachChild(center);
        center.setLocalTranslation(board.xLen() / 2, 0, board.zLen() / 2);

        ChaseCamera chaseCam = new ChaseCamera(t3tris.getCamera(), center, inputManager);
        chaseCam.setSmoothMotion(true);
        chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(board.yLen() / 4));

        float minDistance = (float) Math
                .sqrt(board.xLen() * board.xLen() + board.yLen() * board.yLen() + board.zLen() * board.zLen());
        chaseCam.setMinDistance(minDistance * 1.1f);
        chaseCam.setDefaultDistance(minDistance * 1.5f);
        chaseCam.setMaxDistance(minDistance * 2.5f);
        chaseCam.setInvertVerticalAxis(true);
        chaseCam.setRotationSpeed(2.5f);
        chaseCam.setZoomSensitivity(4.0f);
    }

    private void initKeybinds() {
        inputManager.addMapping("RotateX",
                // new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
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
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.4f));
        rootNode.addLight(al);

        SpotLight spot = new SpotLight();
        spot.setSpotRange(1000f); // distance
        spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        spot.setColor(ColorRGBA.White.mult(1.6f)); // light color
        // spot.setPosition(new Vector3f(0, 30, 2)); // shine from camera loc
        spot.setPosition(new Vector3f(-50, 250, 0));
        spot.setDirection((new Vector3f(0, 0, 0)).subtract(new Vector3f(0, 30, 2)).normalize()); // shine forward from
                                                                                                 // camera loc
        rootNode.addLight(spot);

        final int SHADOWMAP_SIZE = 1024;
        SpotLightShadowRenderer slsr = new SpotLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
        slsr.setLight(spot);
        t3tris.getViewPort().addProcessor(slsr);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
        fpp.addFilter(ssaoFilter);
        t3tris.getViewPort().addProcessor(fpp);
    }


    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {

            if (name.equals("RotateX") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    board.currentPiece().rotateX();
                }
            } else if (name.equals("RotateZ") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    board.currentPiece().rotateZ();
                }
            } else if (name.equals("Right") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    board.currentPiece().moveRight();
                }
            } else if (name.equals("Left") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    board.currentPiece().moveLeft();
                }
            } else if (name.equals("Down") && !keyPressed) {
                while (board.currentPieceSelected()) {
                    board.currentPiece().moveDown();
                }
            } else if (name.equals("Front") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    board.currentPiece().moveForward();
                }
            } else if (name.equals("Back") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    board.currentPiece().moveBack();
                }
            }
        }
    };



    public GamePiece getRandomPiece() {
        int rand = (int) (Math.random() * 29) + 1;
        GamePiece piece = null;
        try {
            piece = (GamePiece) Class.forName("com.vian4.t3tris.gamepiece.Piece" + rand).getConstructor(GameBoard.class, ColorRGBA.class, Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(board, getRandomColor(), 2, 23, 0);
        } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | InstantiationException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            piece = new Piece1(board, getRandomColor(), 2, 23, 0);
        }
        return piece;
    }

    public ColorRGBA getRandomColor() {
        return ColorRGBA.Cyan;
    }

    
}