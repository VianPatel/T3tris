package com.vian4.t3tris;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.SpotLightShadowRenderer;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.SpotLight;
import com.vian4.t3tris.gamepiece.Cube;
import com.vian4.t3tris.gamepiece.GamePiece;
import com.vian4.t3tris.gamepiece.JPiece;
import com.vian4.t3tris.gamepiece.LPiece;
import com.vian4.t3tris.gamepiece.TPiece;

public class T3tris extends SimpleApplication {
    private GameBoard board;

    @Override
    public void simpleInitApp() {
        setPauseOnLostFocus(true);
        setDisplayStatView(false);
        //Box box = new Box(0.5f, 0.5f, 0.5f);
        board = new GameBoard(assetManager, 20, 24, 10, 10);


        Node planeNode = new Node();
        int planePadding = 2;
        Geometry plane = new Geometry("Plane", new Quad(2 * planePadding + board.xLen(), 2 * planePadding + board.zLen()));
        plane.rotate(-90 * FastMath.DEG_TO_RAD, -90 * FastMath.DEG_TO_RAD, 0);
        plane.setMaterial(board.getColor(ColorRGBA.Gray));
        planeNode.attachChild(plane);
        planeNode.setLocalTranslation(-planePadding - 0.5f, 0.0f, -planePadding - 0.5f);
        rootNode.attachChild(planeNode);
        planeNode.setShadowMode(ShadowMode.Receive);

        rootNode.attachChild(board.getBoxNode());
        board.getBoxNode().setShadowMode(ShadowMode.CastAndReceive);
        
        flyCam.setEnabled(false);

        Node center = new Node();
        flyCam.setEnabled(false);

        rootNode.attachChild(center);
        center.setLocalTranslation(board.xLen() / 2, 0, board.zLen() / 2);

        ChaseCamera chaseCam = new ChaseCamera(cam, center, inputManager);
        chaseCam.setSmoothMotion(true);
        chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(board.yLen() / 4));

        float minDistance = (float) Math.sqrt(board.xLen() * board.xLen() + board.yLen() * board.yLen() + board.zLen() * board.zLen());
        chaseCam.setMinDistance(minDistance * 1.1f);
        chaseCam.setDefaultDistance(minDistance * 1.5f);
        chaseCam.setMaxDistance(minDistance * 2.5f);
        chaseCam.setInvertVerticalAxis(true);
        chaseCam.setRotationSpeed(2.5f);
        chaseCam.setZoomSensitivity(4.0f);
                
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
        inputManager.addListener(actionListener, "RotateX", "RotateZ", "Right", "Left", "Down", "Front", "Back");


        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.4f));
        rootNode.addLight(al);

        SpotLight spot = new SpotLight();
        spot.setSpotRange(1000f); // distance
        spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        spot.setColor(ColorRGBA.White.mult(1.6f)); // light color
        //spot.setPosition(new Vector3f(0, 30, 2)); // shine from camera loc
        spot.setPosition(new Vector3f(-50, 250, 0));
        spot.setDirection((new Vector3f(0, 0, 0)).subtract(new Vector3f(0, 30, 2)).normalize()); // shine forward from camera loc
        rootNode.addLight(spot);


        final int SHADOWMAP_SIZE=1024;
        SpotLightShadowRenderer slsr = new SpotLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
        slsr.setLight(spot);
        viewPort.addProcessor(slsr);


        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
        fpp.addFilter(ssaoFilter);
        viewPort.addProcessor(fpp);



        inputManager.addMapping("MouseRotateX",
                new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("MouseRotateX_negative",
                new MouseAxisTrigger(MouseInput.AXIS_X, false));
            inputManager.addMapping("MouseRotateY",
                new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("MouseRotateY_negative",
                new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        
        org.lwjgl.input.Mouse.setCursorPosition(Main.WIDTH / 2, Main.HEIGHT / 2);
    }


    private float timeWaited = 0.0f;

    @Override
    public void simpleUpdate(float tpf) {
        timeWaited += tpf;

        //printBoard(board);
        if (timeWaited >= 1) {
            timeWaited -= 1;
            if (!board.currentPieceSelected()) {
                board.setCurrentPiece(getRandomPiece());
            }
            if (board.currentPiece().moveDown()) {
                System.out.println("Game Over!\nThanks for playing.");
                stop();
            }

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
            //board.currentPiece().rotate();
        }
        
    }

    public boolean pieceContainsPt(GamePiece piece, int x, int y, int z) {
        if (piece == null)
            return false;
        for (Point point : piece.getPoints()) {
            if (point.getX() == x && point.getY() == y && point.getZ() == z) {
                return true;
            }
        }
        return false;
    }

    public GamePiece getRandomPiece() {
        int rand = (int) (Math.random() * 4);
        if (rand == 0) {
            return new Cube(board, ColorRGBA.Blue, 2, 23, 0);
        } else if (rand == 1) {
            return new JPiece(board, ColorRGBA.Green, 2, 23, 0);
        } else if (rand == 2) {
            return new LPiece(board, ColorRGBA.Red, 2, 23, 0);
        }
        return new TPiece(board, ColorRGBA.Orange, 2, 23, 0);
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