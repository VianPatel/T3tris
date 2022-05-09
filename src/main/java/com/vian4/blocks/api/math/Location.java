package com.voidcitymc.blocks.api.math;

import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;

public class Location {
    public float x;
    public float y;
    public float z;

    public Location(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(Vector3f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public Vec3i toVec3i() {
        return new Vec3i((int)x, (int)y, (int)z);
    }

    public Vector3f toVector3f() {
        return new Vector3f(x, y, z);
    }
}
