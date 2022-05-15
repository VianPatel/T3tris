package com.vian4.t3tris.game;

public class Point {
    
    private int x, y, z;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void incrementX(int incrementX) {
        x += incrementX;
    }

    public void incrementY(int incrementY) {
        y += incrementY;
    }

    public void incrementZ(int incrementZ) {
        z += incrementZ;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Point clone() {
        return new Point(x, y, z);
    }

}
