package com.vian4.t3tris.gamepiece;

import com.jme3.math.ColorRGBA;
import com.vian4.t3tris.GameBoard;
import com.vian4.t3tris.Point;

public class LPiece extends GamePiece {
    public LPiece(GameBoard gameBoard, ColorRGBA color, int x, int y, int z) {
        super(gameBoard, color, 1, x, y, z);
    }

    @Override
    protected Point[] initPoints(int x, int y, int z) {
        Point[] points = new Point[4];
        points[0] = new Point(x, y - 2, z);
        points[1] = new Point(x, y - 1, z);
        points[2] = new Point(x, y, z);
        points[3] = new Point(x + 1, y, z);
        return points;
    }

    @Override
    protected Point getCenter() {
        return points[selectedPointIndex];
    }

    @Override
    public GamePiece copy() {
        return new LPiece(gameBoard, getColor(), points[0].getX(), points[0].getY(), points[0].getZ());
    }
}
