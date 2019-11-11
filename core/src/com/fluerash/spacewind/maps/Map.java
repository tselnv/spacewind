package com.fluerash.spacewind.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.fluerash.spacewind.Utility;

public class Map {
    private static final String TAG = Map.class.getSimpleName();
    public final static float UNIT_SCALE  = 1/32f;

    private TiledMap tiledMap;
    private static float width;
    private static float height;



    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void setTiledMap(String fileName) {
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

        this.width = mapWidth * tilePixelWidth * UNIT_SCALE;
        this.height = mapHeight * tilePixelHeight * UNIT_SCALE;
    }

    public static float getWidth() {
        return width;
    }

    public static float getHeight() {
        return height;
    }
}

