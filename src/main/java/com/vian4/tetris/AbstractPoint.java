package com.vian4.tetris;

import com.jme3.math.Vector3f;

public abstract class AbstractPoint {
    protected int x, y, z;

    public AbstractPoint(int x, int y, int z) {
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

}
