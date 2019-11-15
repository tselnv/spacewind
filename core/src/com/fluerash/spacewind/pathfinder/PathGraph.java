package com.fluerash.spacewind.pathfinder;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.fluerash.spacewind.maps.Map;

import java.util.ArrayList;

public class PathGraph  implements IndexedGraph<PathNode> {

    private PathHeuristic pathHeuristic = new PathHeuristic();
    private Array<PathNode> nodes = new Array<>();
    private Array<Road> roads = new Array<>();
    private Map map;
    ArrayList<PathNode[]> nodesMatrix;

    public PathGraph(Map map) {
        this.map = map;
        createPathGraphFromNode();
    }

    private void createPathGraphFromNode(){
        int xSize = (int) map.getWidth();
        int ySize = (int) map.getHeight();

        nodesMatrix = new ArrayList<>(xSize);

        for (int x = 0; x < xSize; x++){
            PathNode[] nodesColumn = new PathNode[ySize];
            for (int y = 0; y < ySize; y++){
                nodesColumn[y] = new PathNode(x,y);
                addPathNode(nodesColumn[y]);
            }
            nodesMatrix.add(nodesColumn);
        }

        for (int x = 0; x < xSize; x++){
            for (int y = 0; y < ySize; y++){
                if (x > 0){
                    connectNodes(nodesMatrix.get(y)[x], nodesMatrix.get(y)[x-1]);
                    connectNodes(nodesMatrix.get(y)[x-1], nodesMatrix.get(y)[x]);
                }
                if (y > 0){
                    connectNodes(nodesMatrix.get(y)[x], nodesMatrix.get(y-1)[x]);
                    connectNodes(nodesMatrix.get(y-1)[x], nodesMatrix.get(y)[x]);
                }
            }
        }
    }

    /** Map of Nodes to Roads starting in that Node. */
    ObjectMap<PathNode, Array<Connection<PathNode>>> roadsMap = new ObjectMap<>();
    private int lastNodeIndex = 0;

    public void addPathNode(PathNode pathNode){
        pathNode.index = lastNodeIndex;
        lastNodeIndex++;
        nodes.add(pathNode);
    }

    public void connectNodes(PathNode fromNode, PathNode toNode){
        Road road = new Road(fromNode, toNode);
        if(!roadsMap.containsKey(fromNode)){
            roadsMap.put(fromNode, new Array<Connection<PathNode>>());
        }
        roadsMap.get(fromNode).add(road);
        roads.add(road);
    }

    private GraphPath<PathNode> findPath(PathNode startPathNode, PathNode goalPathNode){
        GraphPath<PathNode> graphPath = new DefaultGraphPath<>();
        new IndexedAStarPathFinder<>(this).searchNodePath(startPathNode, goalPathNode, pathHeuristic, graphPath);
        return graphPath;
    }

    public GraphPath<PathNode> findPath(Vector2 start, Vector2 goal){
        PathNode nodeStart = getPathNode(start);
        PathNode nodeGoal = getPathNode(goal);

        if (nodeStart == null || nodeGoal == null)
            return null;

        GraphPath<PathNode> graphPath = new DefaultGraphPath<>();
        new IndexedAStarPathFinder<>(this).searchNodePath(nodeStart, nodeGoal, pathHeuristic, graphPath);
        return graphPath;
    }

    @Override
    public int getIndex(PathNode node) {
        return node.index;
    }

    @Override
    public int getNodeCount() {
        return lastNodeIndex;
    }

    @Override
    public Array<Connection<PathNode>> getConnections(PathNode fromNode) {
        if(roadsMap.containsKey(fromNode)){
            return roadsMap.get(fromNode);
        }
        return new Array<>(0);
    }

    public PathNode getPathNode(Vector2 coords){
        int x =(int) (coords.x);
        int y =(int) (coords.y);

        if (nodesMatrix != null && nodesMatrix.size() != 0 && x >= 0 && x < nodesMatrix.size() && y >= 0 && y< nodesMatrix.get(0).length)
            return nodesMatrix.get(x)[y];
        else
            return null;
    }
}
