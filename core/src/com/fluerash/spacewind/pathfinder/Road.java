package com.fluerash.spacewind.pathfinder;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;


public class Road implements Connection<PathNode> {

    private PathNode fromNode;
    private PathNode toNode;
    float cost;

    public Road(PathNode fromNode, PathNode toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = Vector2.dst(fromNode.x, fromNode.y, toNode.x, toNode.y);;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public PathNode getFromNode() {
        return fromNode;
    }

    @Override
    public PathNode getToNode() {
        return toNode;
    }
}
