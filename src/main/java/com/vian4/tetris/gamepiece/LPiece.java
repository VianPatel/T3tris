package com.vian4.tetris.gamepiece;

import com.jme3.math.ColorRGBA;
import com.vian4.tetris.GameBoard;
import com.vian4.tetris.Point;

public class LPiece extends GamePiece {
    public LPiece(GameBoard gameBoard, ColorRGBA color, int x, int y) {
        super(gameBoard, color);
        points = new Point[4];
        points[0] = new Point(x, y);
        points[1] = new Point(x, y + 1);
        points[2] = new Point(x, y + 2);
        points[3] = new Point(x + 1, y + 2);

        selectedPointIndex = 1;
    }

    @Override
    public GamePiece copy() {
        return new LPiece(gameBoard, getColor(), points[0].getX(), points[0].getY());
    }
}
