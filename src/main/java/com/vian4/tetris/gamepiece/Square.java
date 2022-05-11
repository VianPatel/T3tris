package com.vian4.tetris.gamepiece;

import com.vian4.tetris.GameBoard;
import com.vian4.tetris.Point;

public class Square extends GamePiece {
    public Square(GameBoard gameBoard, int x, int y) {
        super(gameBoard);
        points = new Point[4];
        points[0] = new Point(x, y);
        points[1] = new Point(x, y + 1);
        points[2] = new Point(x + 1, y);
        points[3] = new Point(x + 1, y + 1);
    }

    @Override
    public boolean rotate() {
        return true;
    }

    @Override
    public GamePiece copy() {
        return new LPiece(gameBoard, points[0].getX(), points[0].getY());
    }
}