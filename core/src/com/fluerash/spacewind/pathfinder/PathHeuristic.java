package com.fluerash.spacewind.pathfinder;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

public class PathHeuristic implements Heuristic<PathNode> {

    @Override
    public float estimate(PathNode currentNode, PathNode goalNode) {
        return Vector2.dst( currentNode.x, currentNode.y, goalNode.x, goalNode.y);
    }

}
