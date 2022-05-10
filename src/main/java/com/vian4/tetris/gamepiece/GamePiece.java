package com.vian4.tetris.gamepiece;

import com.vian4.tetris.GameBoard;
import com.vian4.tetris.Point;
import com.vian4.tetris.Color;

public abstract class GamePiece {

    private GameBoard gameBoard;
    protected Point[] points;
    protected int selectedPointIndex;

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

    //todo: fix issue with validation
    private boolean validate(Point[] points) {
        for (Point validatePoint : points) {
            if (validatePoint.getY() >= gameBoard.getBoard().length ||
                    validatePoint.getY() < 0 ||
                    validatePoint.getX() >= gameBoard.getBoard()[0].length ||
                    validatePoint.getX() < 0 ||
                    gameBoard.getBoard()[validatePoint.getY()][validatePoint.getX()].isOccupied()) {
                return false;
            }
        }
        return true;
    }

    public boolean rotate() {
        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (i != selectedPointIndex) {
                newPoints[i] = rotateClockwise(points[selectedPointIndex], points[i]);
            } else {
                newPoints[i] = points[i];
            }
        }

        if (!validate(newPoints)) return false;
        points = newPoints;
        return true;
    }

    private boolean move(int amount) {
        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            newPoints[i] = new Point(points[i].getX() + amount, points[i].getY());
        }

        if (!validate(newPoints)) return false;

        points = newPoints;
        return true;
    }

    public boolean moveRight() {
        return move(1);
    }

    public boolean moveLeft() {
        return move(-1);
    }

    private Point rotateClockwise(Point center, Point point) {
        int xDif = point.getX() - center.getX();
        int yDif = point.getY() - center.getY();
        return new Point(center.getX() + yDif, center.getY() - xDif);
    }
}
