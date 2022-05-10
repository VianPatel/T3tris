package com.vian4.tetris.gamepiece;

import com.vian4.tetris.GameBoard;
import com.vian4.tetris.Point;
import com.vian4.tetris.Color;

public abstract class GamePiece {

    private GameBoard gameBoard;
    protected Point[] points;
    protected Point selectedPoint;

    public GamePiece(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

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

    public boolean rotate() {
        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (points[i] != selectedPoint) {
                newPoints[i] = rotateClockwise(selectedPoint, points[i]);
            } else {
                newPoints[i] = points[i];
            }
        }
        
        for (Point validatePoint: newPoints) {
            if (validatePoint.getY() >= gameBoard.getBoard().length || validatePoint.getX() >= gameBoard.getBoard()[0].length) {
                return false;
            }
        }

        points = newPoints;
        return true;
    }

    private Point rotateClockwise(Point center, Point point) {
        int xDif = point.getX() - center.getX();
        int yDif = point.getY() - center.getY();
        return new Point(center.getX() + yDif, center.getY() - xDif);
    }
}
