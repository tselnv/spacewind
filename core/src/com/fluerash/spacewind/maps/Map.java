package com.fluerash.spacewind.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.fluerash.spacewind.Utility;
import com.fluerash.spacewind.pathfinder.PathFinding;
import com.fluerash.spacewind.pathfinder.PathGraph;

public class Map {

    private static final String TAG = Map.class.getSimpleName();
    public final static float UNIT_SCALE  = 1/32f;

    //Map layers
    protected final static String COLLISION_LAYER = "MAP_COLLISION_LAYER";

    private String mapPath;
    private TiledMap tiledMap;
    private static float width;
    private static float height;

    private static float widthInPixel;
    private static float heightInPixel;

    private MapLayer collisionLayer = null;

    private PathGraph pathGraph;

    public Map(String mapPath) {
        this.mapPath = mapPath;


        setTiledMap(mapPath);
        pathGraph = new PathGraph(this);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    private void setTiledMap(String fileName) {
        this.tiledMap = Utility.getMapAsset(fileName);
        if( fileName == null || fileName.isEmpty() ) {
            Gdx.app.debug(TAG, "Map is invalid");
            return;
        }
        Utility.getMapAsset(fileName);

        int mapWidth = tiledMap.getProperties().get("width", Integer.class);
        int mapHeight = tiledMap.getProperties().get("height", Integer.class);
        int tilePixelWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tilePixelHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        this.widthInPixel = mapWidth * tilePixelWidth;
        this.heightInPixel = mapHeight * tilePixelHeight;

        this.width = mapWidth * tilePixelWidth * UNIT_SCALE;
        this.height = mapHeight * tilePixelHeight * UNIT_SCALE;

        collisionLayer = tiledMap.getLayers().get(COLLISION_LAYER);
        if( collisionLayer == null ){
            Gdx.app.debug(TAG, "No collision layer!");
        }
    }

    public static float getWidth() {
        return width;
    }

    public static float getHeight() {
        return height;
    }

    public static float getWidthInPixel() {
        return widthInPixel;
    }

    public static float getHeightInPixel() {
        return heightInPixel;
    }

    public MapLayer getCollisionLayer() {
        return collisionLayer;
    }

    public PathGraph getPathGraph() {
        return pathGraph;
    }
}

