package com.vian4.tetris;

import java.util.HashMap;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.shadow.SpotLightShadowRenderer;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.SpotLight;
import com.vian4.tetris.gamepiece.GamePiece;
import com.vian4.tetris.gamepiece.LPiece;
import com.vian4.tetris.gamepiece.Square;

public class ThreeDTetris extends SimpleApplication {

    private HashMap<ColorRGBA, Material> colorMap = new HashMap<>();

    private Node camNodeRotated;
    private Node camNode;

    private GameBoard board;
    private Geometry[][] boxes;
    private GamePiece[] pieces = new GamePiece[2];
    private Node boxNode = new Node();

    @Override
    public void simpleInitApp() {
        //Box box = new Box(0.5f, 0.5f, 0.5f);
        board = new GameBoard(30, 8);

        rootNode.attachChild(boxNode);
        boxNode.setShadowMode(ShadowMode.CastAndReceive);
        
        Geometry box = (Geometry) assetManager.loadModel("Point.obj");

        flyCam.setEnabled(false);
        camNodeRotated = new CameraNode("Camera Node", cam);
        camNode = new Node();
        camNode.attachChild(camNodeRotated);

        rootNode.attachChild(camNode);
        camNodeRotated.setLocalTranslation(new Vector3f(-50.0f, (float) (board.getBoard().length*1.2), 0.0f));
        //camNodeRotated.lookAt(new Vector3f(0,10,0), new Vector3f(0, 1, 0));

        pieces[0] = new LPiece(board, ColorRGBA.Red, 2, 25);
        pieces[1] = new Square(board, ColorRGBA.Blue, 2, 25);

        boxes = new Geometry[board.getBoard().length][board.getBoard()[0].length];

        for (int r = 0; r < boxes.length; r++) {
            for (int c = 0; c < boxes[r].length; c++) {
                boxes[r][c] = box.clone();//new Geometry("Box", box);
            }
        }


        inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_UP), 
                new KeyTrigger(KeyInput.KEY_W),
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Right",
                new KeyTrigger(KeyInput.KEY_RIGHT),
                new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Left",
                new KeyTrigger(KeyInput.KEY_LEFT),
                new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Down",
                new KeyTrigger(KeyInput.KEY_DOWN),
                new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(actionListener, "Rotate", "Right", "Left", "Down");

        SpotLight spot = new SpotLight();
        spot.setSpotRange(1000f); // distance
        spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        spot.setColor(ColorRGBA.White.mult(1.3f)); // light color
        //spot.setPosition(new Vector3f(0, 30, 2)); // shine from camera loc
        spot.setPosition(camNodeRotated.getWorldTranslation().add(new Vector3f(-50, 250, 0)));
        Vector3f originDir = (new Vector3f(0, 0, 0)).subtract(new Vector3f(0, 30, 2)).normalize();
        System.out.println(originDir);
        spot.setDirection(originDir); // shine forward from camera loc
        rootNode.addLight(spot);


        final int SHADOWMAP_SIZE=1024;
        SpotLightShadowRenderer slsr = new SpotLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
        slsr.setLight(spot);
        viewPort.addProcessor(slsr);


        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
        fpp.addFilter(ssaoFilter);
        viewPort.addProcessor(fpp);



        inputManager.addMapping("RotateX",
                new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("RotateX_negative",
                new MouseAxisTrigger(MouseInput.AXIS_X, false));
            inputManager.addMapping("RotateY",
                new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("RotateY_negative",
                new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addListener(analogListener, "RotateX", "RotateX_negative", "RotateY", "RotateY_negative");        
    }


    private float timeWaited = 0.0f;

    @Override
    public void simpleUpdate(float tpf) {
        timeWaited += tpf;

        if (timeWaited >= 0.2) {
            timeWaited -= 0.2;
            if (!board.currentPieceSelected()) {
                board.setCurrentPiece(pieces[(int)(Math.random()*pieces.length)].copy());
            }
            printBoard(board);
            if (!board.currentPiece().moveDown()) {
                //piece has fallen to bottom (set a new piece)
                //todo: make random
            }
            //board.currentPiece().rotate();
        }
        
    }

    public void printBoard(GameBoard board) {
        boxNode.detachAllChildren();
        for (int y = board.getBoard().length - 1; y >= 0; y--) {
            for (int x = 0; x < board.getBoard()[y].length; x++) {
                if (board.getBoard()[y][x].isOccupied() || pieceContainsPt(board.currentPiece(), x, y)) {
                    ColorRGBA pointColor = null;
                    if (board.getBoard()[y][x].isOccupied()) {
                        pointColor = board.getBoard()[y][x].getColor();
                    } else {
                        pointColor = board.currentPiece().getColor();
                    }

                    boxes[y][x].setMaterial(getColor(pointColor));
                    boxNode.attachChild(boxes[y][x]);
                    boxes[y][x].setLocalTranslation(0, y, x);
                }
            }
        }
    }

    public Material getColor(ColorRGBA color) {
        Material mat = colorMap.get(color);
        if (mat == null) {
            mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setBoolean("UseMaterialColors", true);
            mat.setColor("Ambient", color);
            mat.setColor("Diffuse", color);
            colorMap.put(color, mat);
        }
        return mat;
    }

    public boolean pieceContainsPt(GamePiece piece, int x, int y) {
        if (piece == null)
            return false;
        for (Point point : piece.getPoints()) {
            if (point.getX() == x && point.getY() == y)
                return true;
        }
        return false;
    }

    private final AnalogListener analogListener = new AnalogListener() {
        boolean movedToCenter = false;

        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (movedToCenter) {
                movedToCenter = false;
                return;
            }
            if ("RotateX".equals(name)) {
                camNode.rotate(0, (float) (value * speed * 8), 0);
            } else if ("RotateX_negative".equals(name)) {
                camNode.rotate(0, (float) (-value * speed * 8), 0);
            } else if ("RotateY".equals(name)) {
                camNode.rotate(0, 0, (float) (-value * speed));
            } else if ("RotateY_negative".equals(name)) {
                camNode.rotate(0, 0, (float) (value * speed));
//                org.lwjgl.input.Mouse.setCursorPosition(Main.WIDTH / 2, Main.HEIGHT / 2);
            }
            camNodeRotated.lookAt(new Vector3f(0, 10, 0), new Vector3f(0, 1, 0));
        }
    };

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {

            if (name.equals("Rotate") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    board.currentPiece().rotate();
                }
            } else if (name.equals("Right") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    board.currentPiece().moveRight();
                }
            } else if (name.equals("Left") && !keyPressed) {
                if (board.currentPieceSelected()) {
                    board.currentPiece().moveLeft();
                }
            } else if (name.equals("Down")) {

            }
        }
    };
}

/*


import com.vian4.tetris.gamepiece.GamePiece;
import com.vian4.tetris.gamepiece.LPiece;
import com.vian4.tetris.gamepiece.Square;

public class Main {

    public static void main(String[] args) {
        GameBoard board = new GameBoard();
        board.setCurrentPiece(new Square(board, 3, 8));
        while (true) {
            try {
                Thread.sleep(500);
                printBoard(board);
                if (!board.currentPiece().moveDown()) {
                    //reached end
                    printBoard(board);
                    
                    
                    board.setCurrentPiece(new LPiece(board, 2, 5));
                }
                board.currentPiece().rotate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printBoard(GameBoard board) {
        System.out.println("\n\n\n\n\n\n\n");
        for (int y = board.getBoard().length-1; y >= 0; y--) {
            for (int x = 0; x < board.getBoard()[y].length; x++) {
                if ( (board.getBoard()[y][x].getColor() != null && board.getBoard()[y][x].getColor().a == 1) || pieceContainsPt(board.currentPiece(), x, y)) {
                    System.out.print("x");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }

    public static boolean pieceContainsPt(GamePiece piece, int x, int y) {
        if (piece == null) return false;
        for (Point point: piece.getPoints()) {
            if (point.getX() == x && point.getY() == y) return true;
        }
        return false;
    }
}
*/