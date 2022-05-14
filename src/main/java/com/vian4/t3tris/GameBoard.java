package com.vian4.t3tris;

import java.util.HashMap;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.vian4.t3tris.gamepiece.GamePiece;

public class GameBoard {
    private HashMap<ColorRGBA, Material> colorMap = new HashMap<>();

    private Node boxNode;
    
    private Geometry pointBoxShape;
    private Geometry blankBoxShape;
    private Geometry wireframeBoxShape;

    private AssetManager assetManager;

    private Geometry[][][] board;
    private GamePiece currentPiece = null;
    public final int maxHeight;

    public GameBoard(AssetManager assetManager, int maxHeight, int r, int c, int d) {
        this.boxNode = new Node();

        this.maxHeight = maxHeight;
        board = new Geometry[r][c][d];

        this.assetManager = assetManager;

        pointBoxShape = (Geometry) assetManager.loadModel("Point.obj");
        blankBoxShape = (Geometry) assetManager.loadModel("Box.obj");

        Geometry wireframeBoxShape = new Geometry("coordinate axis", blankBoxShape.getMesh());
        //wireframeBoxShape.setLocalTranslation(x, y, z);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setLineWidth(1);
        mat.setColor("Color", ColorRGBA.Gray);
        wireframeBoxShape.setMaterial(mat);
        this.wireframeBoxShape = wireframeBoxShape;
    }

    public Node getBoxNode() {
        return boxNode;
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

    public Geometry getBoxShape() {
        return pointBoxShape.clone();
    }

    public Geometry getWireframeBoxShape() {
        return wireframeBoxShape.clone();
    }
    
    public boolean currentPieceSelected() {
        return currentPiece != null;
    }

    public GamePiece currentPiece() {
        return currentPiece;
    }

    public void setCurrentPiece(GamePiece piece) {
        currentPiece = piece;
    }

    public int yLen() {
        return board.length;
    }

    public int xLen() {
        return board[0].length;
    }

    public int zLen() {
        return board[0][0].length;
    }

    public void setSlot(int y, int x, int z, Geometry slot) {
        boxNode.attachChild(slot);
        board[y][x][z] = slot;
    }

    public Geometry getSlot(int y, int x, int z) {
        return board[y][x][z];
    }

    public void clearYPlain(int y) {
        for (y = y + 1; y < board.length; y ++) {
            for (int x = 0; x < board[y].length; x++) {
                for (int z = 0; z < board[y][x].length; z++) {
                    board[y-1][x][z] = board[y][x][z];
                }
            }
        }
        
        for (int x = 0; x < board[y - 1].length; x++) {
            for (int z = 0; z < board[y - 1][x].length; z++) {
                board[board.length - 1][x][z] = null;
            }
        }
    }

    public void clearXPlain(int x) {
        for (int y = 0; y < board.length; y++) {
            for (int z = 0; z < board[y][x].length; z++) {
                board[y][x][z] = null;
            }
        }
    }

    public void clearZPlain(int z) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x][z] = null;
            }
        }
    }

    public boolean isYPlainFilled(int y) {
        for (int x = 0; x < board[y].length; x++) {
            for (int z = 0; z < board[y][x].length; z++) {
                if (board[y][x][z] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isXPlainFilled(int x) {
        for (int y = 0; y < board.length; y++) {
            for (int z = 0; z < board[y][x].length; z++) {
                if (board[y][x][z] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isZPlainFilled(int z) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x][z] == null) {
                    return false;
                }
            }
        }
        return true;
    }

}
