package com.vian4.tetris;

import java.util.HashMap;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.system.AppSettings;
import com.vian4.tetris.gamepiece.GamePiece;
import com.vian4.tetris.gamepiece.LPiece;
import com.vian4.tetris.gamepiece.Square;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("3D Tetris");
        settings.put("Width", 1280);
        settings.put("Height", 720);
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();
    }

    HashMap<ColorRGBA, Material> colorMap = new HashMap<>();

    private GameBoard board;
    private Geometry[][] boxes;
    private GamePiece[] pieces = new GamePiece[2];

    @Override
    public void simpleInitApp() {
        Box box = new Box(0.5f, 0.5f, 0.5f);

        flyCam.setEnabled(false);
        Node camNode = new CameraNode("Camera Node", cam);
        rootNode.attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(-30, 20, 0));
        camNode.lookAt(new Vector3f(0,10,0), new Vector3f(0, 1, 0));

        board = new GameBoard(30, 8);

        pieces[0] = new LPiece(board, ColorRGBA.Red, 2, 25);
        pieces[1] = new Square(board, ColorRGBA.Blue, 2, 25);

        boxes = new Geometry[board.getBoard().length][board.getBoard()[0].length];

        for (int r = 0; r < boxes.length; r++) {
            for (int c = 0; c < boxes[r].length; c++) {
                boxes[r][c] = new Geometry("Box", box);
            }
        }

        Material matBlue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matBlue.setColor("Color", ColorRGBA.Blue);
        colorMap.put(ColorRGBA.Blue, matBlue);

        Material matRed = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matRed.setColor("Color", ColorRGBA.Red);
        colorMap.put(ColorRGBA.Red, matRed);

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
        rootNode.detachAllChildren();
        for (int y = board.getBoard().length - 1; y >= 0; y--) {
            for (int x = 0; x < board.getBoard()[y].length; x++) {
                if (board.getBoard()[y][x].isOccupied() || pieceContainsPt(board.currentPiece(), x, y)) {
                    ColorRGBA pointColor = null;
                    if (board.getBoard()[y][x].isOccupied()) {
                        pointColor = board.getBoard()[y][x].getColor();
                    } else {
                        pointColor = board.currentPiece().getColor();
                    }

                    boxes[y][x].setMaterial(colorMap.get(pointColor));
                    rootNode.attachChild(boxes[y][x]);
                    boxes[y][x].setLocalTranslation(0, y, x);
                }
            }
        }
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