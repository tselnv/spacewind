package com.fluerash.spacewind;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    public static final AssetManager assetManager = new AssetManager();
    private static InternalFileHandleResolver filePathResolver =  new InternalFileHandleResolver();

    private static TextureAtlas  textureCharactersAtlas;


    public static TiledMap getMapAsset(String mapFilenamePath){
        loadMapAsset(mapFilenamePath);
        TiledMap map = null;

        if( assetManager.isLoaded(mapFilenamePath)){
            map = assetManager.get(mapFilenamePath, TiledMap.class);
        } else{
            Gdx.app.debug(TAG, "Map is not loaded: " + mapFilenamePath);
        }
        return map;
    }

    public static void loadMapAsset(String mapFilenamePath){
        if( mapFilenamePath == null || mapFilenamePath.isEmpty() ){
            return;
        }

        if( assetManager.isLoaded(mapFilenamePath) ){
            return;
        }

        //load asset
        if( filePathResolver.resolve(mapFilenamePath).exists() ){
            assetManager.setLoader(TiledMap.class, new TmxMapLoader(filePathResolver));
            assetManager.load(mapFilenamePath, TiledMap.class);
            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(mapFilenamePath);
            Gdx.app.debug(TAG, "Map loaded!: " + mapFilenamePath);
        }
        else{
            Gdx.app.debug(TAG, "Map doesn't exist!: " + mapFilenamePath );
        }
    }

    public static void loadTextureAsset(String textureFilenamePath){
        if( textureFilenamePath == null || textureFilenamePath.isEmpty() ){
            return;
        }

        if( assetManager.isLoaded(textureFilenamePath) ){
            return;
        }

        //load asset
        if( filePathResolver.resolve(textureFilenamePath).exists() ){
            assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
            assetManager.load(textureFilenamePath, Texture.class);
            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(textureFilenamePath);
        }
        else{
            Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureFilenamePath );
        }
    }

    public static Texture getTextureAsset(String textureFilenamePath){
        Texture texture = null;

        // once the asset manager is done loading
        if( assetManager.isLoaded(textureFilenamePath) ){
            texture = assetManager.get(textureFilenamePath,Texture.class);
        } else {
            Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilenamePath );
        }

        return texture;
    }


    public static TextureAtlas getTextureCharactersAtlas(){
        if (textureCharactersAtlas == null){
            textureCharactersAtlas = new TextureAtlas(Gdx.files.internal("characters/micha.pack.atlas"));
        }
        return textureCharactersAtlas;
    }


}
