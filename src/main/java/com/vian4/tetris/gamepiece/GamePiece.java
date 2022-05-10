package com.vian4.tetris.gamepiece;

import com.vian4.tetris.GameBoard;
import com.vian4.tetris.Point;
import com.vian4.tetris.Color;

public abstract class GamePiece {

    private GameBoard gameBoard;
    protected Point[] points;

    public GamePiece(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public abstract boolean rotate();

    public boolean moveDown() {
        boolean moved = true;
        for (Point point: points) {
            if (point.getY() < 1 || gameBoard.getBoard()[point.getY()-1][point.getX()].isOccupied()) {
                moved = false;
                break;
            }
        }

        if (!moved) {
            //freeze object in place
            for (Point point: points) {
                gameBoard.getBoard()[point.getY()][point.getX()].setOccupied(new Color((byte)0, (byte)0, (byte)0, (byte)1));
                gameBoard.setCurrentPiece(null);
            }
            return false;
        }

        for (Point point: points) {
            point.incrementY(-1);
        }
        return true;
    }

    public Point[] getPoints() {
        return points;
    }
}
