package com.vian4.t3tris;

public class Point extends AbstractPoint {
    
    public Point(int x, int y, int z) {
        super(x, y, z);
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

}
