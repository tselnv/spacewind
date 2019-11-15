package com.fluerash.spacewind.pathfinder;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.fluerash.spacewind.ai_test.CityGraph;
import com.fluerash.spacewind.maps.Map;

import java.util.ArrayList;

public class PathFinding {

    PathGraph pathGraph;
    GraphPath<PathNode> path;

    public PathFinding(Map map) {
        pathGraph = new PathGraph(map);

        int xSize = (int) map.getWidth();
        int ySize = (int) map.getHeight();

        ArrayList<PathNode[]> nodesMatrix = new ArrayList<>(xSize);

        for (int x = 0; x < xSize; x++){
            PathNode[] nodesColumn = new PathNode[ySize];
            for (int y = 0; y < xSize; y++){
                nodesColumn[y] = new PathNode(x,y);
                pathGraph.addPathNode(nodesMatrix.get(y)[x]);
            }
            nodesMatrix.add(nodesColumn);
        }

        for (int x = 0; x < xSize; x++){
            for (int y = 0; y < xSize; y++){
                if (x > 0){
                    pathGraph.connectNodes(nodesMatrix.get(y)[x], nodesMatrix.get(y)[x-1]);
                    pathGraph.connectNodes(nodesMatrix.get(y)[x-1], nodesMatrix.get(y)[x]);
                }
                if (y > 0){
                    pathGraph.connectNodes(nodesMatrix.get(y)[x], nodesMatrix.get(y-1)[x]);
                    pathGraph.connectNodes(nodesMatrix.get(y-1)[x], nodesMatrix.get(y)[x]);
                }
            }
        }
    }

    public void se(String[] args) {
        //PathFinding pathFinding = new PathFinding();
    }
}
