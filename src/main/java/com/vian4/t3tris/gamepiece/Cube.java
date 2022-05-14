package com.vian4.t3tris.gamepiece;

import com.jme3.math.ColorRGBA;
import com.vian4.t3tris.GameBoard;
import com.vian4.t3tris.Point;

public class Cube extends GamePiece {
    public Cube(GameBoard gameBoard, ColorRGBA color, int x, int y, int z) {
        super(gameBoard, color, -1, x, y, z);
    }

    @Override
    protected Point[] initPoints(int x, int y, int z) {
        Point[] points = new Point[8];
        points[0] = new Point(x, y - 1, z);
        points[1] = new Point(x, y , z);
        points[2] = new Point(x + 1, y - 1, z);
        points[3] = new Point(x + 1, y, z);
        
        points[4] = new Point(x, y - 1, z + 1);
        points[5] = new Point(x, y, z + 1);
        points[6] = new Point(x + 1, y - 1, z + 1);
        points[7] = new Point(x + 1, y, z + 1);
        return points;
    }

    @Override
    protected Point getCenter() {
        return null;
    }

    @Override
    public GamePiece copy() {
        return new Cube(gameBoard, getColor(), points[0].getX(), points[0].getY(), points[0].getZ());
    }
}
