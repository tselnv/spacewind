package com.fluerash.spacewind.pathfinder;

import com.badlogic.gdx.math.Vector2;

public class PathNode {
    float x;
    float y;

    /** Index used by the A* algorithm. Keep track of it so we don't have to recalculate it later. */
    int index;

    public PathNode(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float dist(PathNode other) {
        return Vector2.dst( this.x, this.y, other.x, other.y);
    }

    public String getName() {
        return "(" + x + ":" + y + ")";
    }

    public Vector2 getVector(){
        return new Vector2(x+1, y+1);
    }
}
