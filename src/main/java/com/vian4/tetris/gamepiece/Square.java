package com.vian4.tetris.gamepiece;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.vian4.tetris.GameBoard;
import com.vian4.tetris.Point;

public class Square extends GamePiece {
    public Square(GameBoard gameBoard, ColorRGBA color, int x, int y, int z) {
        super(gameBoard, color, -1, x, y, z);
    }

    @Override
    protected Point[] initPoints(int x, int y, int z) {
        Point[] points = new Point[4];
        points[0] = new Point(x, y, z);
        points[1] = new Point(x, y + 1, z);
        points[2] = new Point(x + 1, y, z);
        points[3] = new Point(x + 1, y + 1, z);
        return points;
    }

    @Override
    protected Vector3f getCenter() {
        return new Vector3f(
                (points[0].getX() + points[1].getX() + points[2].getX() + points[3].getX()) / 4.0f,
                (points[0].getY() + points[1].getY() + points[2].getY() + points[3].getY()) / 4.0f,
                (points[0].getZ() + points[1].getZ() + points[2].getZ() + points[3].getZ()) / 4.0f);
    }

    @Override
    public GamePiece copy() {
        return new Square(gameBoard, getColor(), points[0].getX(), points[0].getY(), points[0].getZ());
    }
}
