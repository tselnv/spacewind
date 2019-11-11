package com.fluerash.spacewind;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.fluerash.spacewind.maps.Map;
import com.fluerash.spacewind.maps.NPCSimple;

import java.util.ArrayList;

public class MainGameScreen implements Screen {
    private static final String TAG = MainGameScreen.class.getSimpleName();

    private static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    private SpriteBatch batch;
    private Texture img;
    private OrthographicCamera camera;
    private Map map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private ArrayList<NPCSimple> npcList;

    @Override
    public void show() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        map = new Map();
        map.setTiledMap("map.tmx");

        //camera setup
        setupViewport(10, 10);

        //get the current size
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        mapRenderer = new OrthogonalTiledMapRenderer(map.getTiledMap(), Map.UNIT_SCALE);
        mapRenderer.setView(camera);
        mapRenderer.setMap(map.getTiledMap());
        Gdx.app.debug(TAG, "UnitScale value is: " + mapRenderer.getUnitScale());

        npcList = new ArrayList<>();

        for(int i = 0; i < 20000; i++) {
            npcList.add(new NPCSimple());
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        mapRenderer.getBatch().enableBlending();
        mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        mapRenderer.render();

        for(NPCSimple npc: npcList) {
            npc.update(map, mapRenderer.getBatch(), delta);
        }
    }

    private void updateCameraPosition(float delta, int x, int y){
        float cameraMovementSpeed = VIEWPORT.virtualHeight;
        float deltaMove = delta * cameraMovementSpeed;
        camera.position.set(camera.position.x + x * deltaMove , camera.position.y + y * deltaMove, 0f);
        camera.update();
    }

    private void updateViewport(float delta, int scale){
        setupViewport((int)VIEWPORT.viewportWidth + scale, (int)VIEWPORT.viewportHeight + scale);
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        camera.update();
    }

    private void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            updateCameraPosition(delta, -1,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            updateCameraPosition(delta, 1,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            updateCameraPosition(delta, 0,1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            updateCameraPosition(delta, 0,-1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)){
            updateViewport(delta, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)){
            updateViewport(delta, -1);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    private void setupViewport(int width, int height){
        //Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;

        //Current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;

        //pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

        //aspect ratio for current viewport
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

        //update viewport if there could be skewing
        if( VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio){
            //Letterbox left and right
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth/VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        }else{
            //letterbox above and below
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight/VIEWPORT.physicalWidth);
        }

        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")" );
        Gdx.app.debug(TAG, "WorldRenderer: viewport: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")" );
        Gdx.app.debug(TAG, "WorldRenderer: physical: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")" );
    }
}
