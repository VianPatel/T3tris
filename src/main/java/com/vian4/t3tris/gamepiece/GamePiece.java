package com.vian4.t3tris.gamepiece;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.vian4.t3tris.game.GameBoard;
import com.vian4.t3tris.game.Point;

public abstract class GamePiece {

    protected GameBoard gameBoard;
    protected Point[] points;
    protected Geometry[] shapes;
    protected final int selectedPointIndex;

    private WireframeGamePiece wireframeGamePiece;
    private ColorRGBA color;
    private Node gamePieceNode = new Node();

    public GamePiece(GameBoard gameBoard, ColorRGBA color, int selectedPointIndex, int x, int y, int z) {
        this.points = initPoints(x, y, z);

        wireframeGamePiece = new WireframeGamePiece(gameBoard, this);
        
        shapes = new Geometry[points.length];
        for (int i = 0; i < shapes.length; i++) {
            shapes[i] = gameBoard.getBoxShape();
            shapes[i].setMaterial(gameBoard.getColor(color));
            gamePieceNode.attachChild(shapes[i]);
        }
        gameBoard.getBoxNode().attachChild(gamePieceNode);
        updateNodeTranslation();
        this.gameBoard = gameBoard;
        this.color = color;
        this.selectedPointIndex = selectedPointIndex;
    }

    private void destroy() {
        gameBoard.getBoxNode().detachChild(gamePieceNode);
    }
    
    private void updateNodeTranslation() {
        for (int i = 0; i < points.length; i++) {
            shapes[i].setLocalTranslation(points[i].getX(), points[i].getY(), points[i].getZ());
        }
        wireframeGamePiece.updateNodeTranslation();
    }

    protected abstract Point[] initPoints(int x, int y, int z);

    public Node getNode() {
        return gamePieceNode;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public boolean moveDown() {
        boolean moved = true;
        for (Point point: points) {
            if (point.getY() < 1 || gameBoard.getSlot(point.getY()-1, point.getX(), point.getZ()) != null) {
                moved = false;
                break;
            }
        }

        if (!moved) {
            //freeze object in place
            int maxY = 0;
            for (int i = 0; i < points.length; i++) {
                if (points[i].getY() > maxY) {
                    maxY = points[i].getY();
                }
                gamePieceNode.detachAllChildren();
                wireframeGamePiece.destroy();
                this.destroy();
                gameBoard.setSlot(points[i].getY(), points[i].getX(), points[i].getZ(), shapes[i]);
            }
            gameBoard.setCurrentPiece(null);
            if (maxY >= gameBoard.maxHeight) {
                return true;
            }
            return false;
        }

        for (Point point: points) {
            point.incrementY(-1);
        }

        updateNodeTranslation();
        return false;
    }

    public Point[] getPoints() {
        return points;
    }

    public Geometry[] getShapes() {
        return shapes;
    }

    private boolean validate(Point[] points) {
        for (Point validatePoint : points) {
            if (validatePoint.getY() >= gameBoard.yLen() ||
                    validatePoint.getY() < 0 ||
                    validatePoint.getX() >= gameBoard.xLen() ||
                    validatePoint.getX() < 0 ||
                    validatePoint.getZ() >= gameBoard.zLen() ||
                    validatePoint.getZ() < 0 ||
                    gameBoard.getSlot(validatePoint.getY(), validatePoint.getX(), validatePoint.getZ()) != null) {
                return false;
            }
        }
        return true;
    }

    public final boolean rotateClockwiseX() {
        return rotate(2);
    }

    public final boolean rotateCounterclockwiseX() {
        return rotate(3);
    }

    public final boolean rotateClockwiseZ() {
        return rotate(0);
    }

    public final boolean rotateCounterclockwiseZ() {
        return rotate(1);
    }

    private boolean rotate(int direction) {
        //0 = clockwiseZ, 1 = counterclockwiseZ, 2 = clockwiseX, 3 = counterclockwiseX
        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (i != selectedPointIndex) {
                if (direction == 0) {
                    newPoints[i] = rotateClockwiseZ(points[i]);
                } else if (direction == 1) {
                    newPoints[i] = rotateCounterclockwiseZ(points[i]);
                } else if (direction == 2) {
                    newPoints[i] = rotateClockwiseX(points[i]);
                } else {
                    newPoints[i] = rotateCounterclockwiseX(points[i]);
                }
                if (newPoints[i] == null) return true;
            } else {
                newPoints[i] = points[i];
            }
        }

        if (!validate(newPoints))
            return false;
        points = newPoints;
        updateNodeTranslation();
        return true;
    }

    public boolean moveX(int amount) {
        return move(0, amount);
    }

    public boolean moveZ(int amount) {
        return move(1, amount);
    }

    private boolean move(int direction, int amount) {
        // 0 = x, 1 = z;
        Point[] newPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (direction == 0) {
                newPoints[i] = new Point(points[i].getX() + amount, points[i].getY(), points[i].getZ());
            } else {
                newPoints[i] = new Point(points[i].getX(), points[i].getY(), points[i].getZ() + amount);
            }
        }

        if (!validate(newPoints))
            return false;

        points = newPoints;
        updateNodeTranslation();
        return true;
    }

    private Point rotateClockwiseX(Point point) {
        Point center = getCenter();
        if (center == null)
            return null;
        int xDif = point.getX() - center.getX();
        int yDif = point.getY() - center.getY();
        return new Point(center.getX() + yDif, center.getY() - xDif, point.getZ());
    }

    private Point rotateCounterclockwiseX(Point point) {
        Point center = getCenter();
        if (center == null)
            return null;
        int xDif = point.getX() - center.getX();
        int yDif = point.getY() - center.getY();
        return new Point(center.getX() - yDif, center.getY() + xDif, point.getZ());
    }

    private Point rotateClockwiseZ(Point point) {
        Point center = getCenter();
        if (center == null)
            return null;
        int zDif = point.getZ() - center.getZ();
        int yDif = point.getY() - center.getY();
        return new Point(point.getX(), center.getY() - zDif, center.getZ() + yDif);
    }

    private Point rotateCounterclockwiseZ(Point point) {
        Point center = getCenter();
        if (center == null)
            return null;
        int zDif = point.getZ() - center.getZ();
        int yDif = point.getY() - center.getY();
        return new Point(point.getX(), center.getY() + zDif, center.getZ() - yDif);
    }

    protected Point getCenter() {
        return points[selectedPointIndex];
    }
    
}
