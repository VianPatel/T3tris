package com.vian4.t3tris.gamepiece;

import com.jme3.math.ColorRGBA;
import com.vian4.t3tris.game.GameBoard;
import com.vian4.t3tris.game.Point;

public class Piece14 extends GamePiece {

    public Piece14(GameBoard gameBoard, ColorRGBA color, int x, int y, int z) {
        super(gameBoard, color, 1, x, y, z);
    }

    @Override
    protected Point[] initPoints(int x, int y, int z) {
        Point[] points = new Point[5];
        points[0] = new Point(x, y, z);
        points[1] = new Point(x, y - 1, z);
        points[2] = new Point(x, y - 2, z);
        points[3] = new Point(x + 1, y - 1, z);
        points[4] = new Point(x + 1, y - 1, z + 1);
        return points;
    }

    @Override
    protected Point getCenter() {
        return points[selectedPointIndex];
    }
}