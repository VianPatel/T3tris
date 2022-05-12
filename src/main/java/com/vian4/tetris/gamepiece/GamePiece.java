package com.vian4.tetris.gamepiece;

import com.vian4.tetris.GameBoard;
import com.vian4.tetris.Point;
import com.jme3.math.ColorRGBA;

public abstract class GamePiece {

    protected GameBoard gameBoard;
    protected Point[] points;
    protected final int selectedPointIndex;
    private ColorRGBA color;

    public GamePiece(GameBoard gameBoard, ColorRGBA color, int selectedPointIndex, int x, int y, int z) {
        this.points = initPoints(x, y, z);
        this.gameBoard = gameBoard;
        this.color = color;
        this.selectedPointIndex = selectedPointIndex;
    }

    protected abstract Point[] initPoints(int x, int y, int z);

    public ColorRGBA getColor() {
        return color;
    }

    public boolean moveDown() {
        boolean moved = true;
        for (Point point: points) {
            if (point.getY() < 1 || gameBoard.getBoard()[point.getY()-1][point.getX()][point.getZ()].isOccupied()) {
                moved = false;
                break;
            }
        }

        if (!moved) {
            //freeze object in place
            for (Point point: points) {
                gameBoard.getBoard()[point.getY()][point.getX()][point.getZ()].setOccupied(color);
            }
            gameBoard.setCurrentPiece(null);
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
                    validatePoint.getZ() >= gameBoard.getBoard()[0][0].length ||
                    validatePoint.getZ() < 0 ||
                    gameBoard.getBoard()[validatePoint.getY()][validatePoint.getX()][validatePoint.getZ()].isOccupied()) {
                return false;
            }
        }
        return true;
    }

    public boolean rotateX() {
        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (i != selectedPointIndex) {
                newPoints[i] = rotateClockwiseX(points[selectedPointIndex], points[i]);
            } else {
                newPoints[i] = points[i];
            }
        }

        if (!validate(newPoints)) return false;
        points = newPoints;
        return true;
    }

    public boolean rotateZ() {
        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (i != selectedPointIndex) {
                newPoints[i] = rotateClockwiseZ(points[selectedPointIndex], points[i]);
            } else {
                newPoints[i] = points[i];
            }
        }

        if (!validate(newPoints))
            return false;
        points = newPoints;
        return true;
    }

    private boolean moveX(int amount) {
        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            newPoints[i] = new Point(points[i].getX() + amount, points[i].getY(), points[i].getZ());
        }

        if (!validate(newPoints)) return false;

        points = newPoints;
        return true;
    }

    private boolean moveZ(int amount) {
        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            newPoints[i] = new Point(points[i].getX(), points[i].getY(), points[i].getZ() + amount);
        }

        if (!validate(newPoints))
            return false;

        points = newPoints;
        return true;
    }

    public boolean moveRight() {
        return moveX(1);
    }

    public boolean moveLeft() {
        return moveX(-1);
    }

    public boolean moveBack() {
        return moveZ(1);
    }

    public boolean moveForward() {
        return moveZ(-1);
    }

    private Point rotateClockwiseX(Point center, Point point) {
        int xDif = point.getX() - center.getX();
        int yDif = point.getY() - center.getY();
        return new Point(center.getX() + yDif, center.getY() - xDif, point.getZ());
    }

    private Point rotateClockwiseZ(Point center, Point point) {
        int zDif = point.getZ() - center.getZ();
        int yDif = point.getY() - center.getY();
        return new Point(point.getX(), center.getY() - zDif, center.getZ() + yDif);
    }

    public abstract GamePiece copy();

}
