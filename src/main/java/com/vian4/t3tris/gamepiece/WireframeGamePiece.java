package com.vian4.t3tris.gamepiece;

import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.vian4.t3tris.GameBoard;
import com.vian4.t3tris.Point;

public class WireframeGamePiece {
    private GamePiece piece;
    private GameBoard board;

    private Node wireframeNode = new Node();
    
    private Geometry[] shapes;

    public WireframeGamePiece(GameBoard board, GamePiece piece) {
        this.board = board;
        this.piece = piece;

        shapes = new Geometry[piece.getPoints().length];
        for (int i = 0; i < shapes.length; i++) {
            shapes[i] = board.getWireframeBoxShape();
            //shapes[i].setMaterial(board.getColor(color));
            wireframeNode.attachChild(shapes[i]);
        }
        wireframeNode.setShadowMode(ShadowMode.Off);
        board.getBoxNode().attachChild(wireframeNode);
    }

    protected void delete() {
        board.getBoxNode().detachChild(wireframeNode);
    }

    public void updateNodeTranslation() {
        Point[] points = getPoints();
        for (int i = 0; i < points.length; i++) {
            shapes[i].setLocalTranslation(points[i].getX(), points[i].getY(), points[i].getZ());
        }
    }

    private Point[] getPoints() {
        Point[] points = new Point[piece.getPoints().length];

        for (int i = 0; i < points.length; i++) {
            points[i] = piece.getPoints()[i].clone();
        }

        pointShifter:
        while (true) {
            for (Point point: points) {
                if (point.getY() == 0 || board.getSlot(point.getY() - 1, point.getX(), point.getZ()) != null) {
                    break pointShifter;
                }
            }

            for (Point point : points) {
                point.setY(point.getY() - 1);
            }
        }

        return points;
    }
    
}
