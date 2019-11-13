package com.fluerash.spacewind;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class NPCInputComponent extends InputComponent implements InputProcessor {
    private static final String TAG = NPCInputComponent.class.getSimpleName();

    enum FmState{
        NORMAL, GOTO
    }

    private float frameTime = 0.0f;
    private Vector3 lastMouseCoordinates;

    private FmState fmState = FmState.NORMAL;
    private Vector2 gotoVector;

    public NPCInputComponent() {
        super();
        currentDirection = Entity.Direction.getRandomNext();
        currentState = Entity.State.WALKING;
        this.lastMouseCoordinates = new Vector3();
    }

    @Override
    public void update(Entity entity, float delta) {
        if(keys.get(Keys.QUIT)) {
            Gdx.app.exit();
        }

        //If IMMOBILE, don't update anything
        if( currentState == Entity.State.IMMOBILE ) {
            //entity.sendMessage(MESSAGE.CURRENT_STATE, Entity.State.IMMOBILE);
            entity.setState(Entity.State.IMMOBILE);
            return;
        }

        frameTime += delta;

        //Change direction after so many seconds
        if( frameTime > MathUtils.random(1,5) ){
            switch (fmState) {
                case NORMAL:
                    updateStateAndDirectionAtNormal(entity);
                    break;
                case GOTO:
                    updateStateAndDirectionAtGoto(entity);
                    break;
            }
            frameTime = 0.0f;
            entity.setState(currentState);
            entity.setDirection(currentDirection);
        }


        if( currentState == Entity.State.IDLE ){
            //entity.sendMessage(MESSAGE.CURRENT_STATE, Entity.State.IDLE);
            entity.setState(Entity.State.IDLE);
            return;
        }

        //Mouse input
        if( mouseButtons.get(Mouse.SELECT)) {
            //Gdx.app.debug(TAG, "Mouse LEFT click at : (" + _lastMouseCoordinates.x + "," + _lastMouseCoordinates.y + ")" );
            //entity.sendMessage(MESSAGE.INIT_SELECT_ENTITY, lastMouseCoordinates);
            entity.mouseClick(lastMouseCoordinates);
            mouseButtons.put(Mouse.SELECT, false);
        }

    }

    private void updateStateAndDirectionAtNormal(Entity entity){
        currentState = Entity.State.getRandomNext();
        currentDirection = Entity.Direction.getRandomNext();
    }

    private void updateStateAndDirectionAtGoto(Entity entity){
        currentDirection = entity.findPath(gotoVector);
        currentState = Entity.State.WALKING;
    }

    @Override
    public boolean keyDown(int keycode) {
        if( keycode == Input.Keys.Q){
            keys.put(Keys.QUIT, true);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Gdx.app.debug(TAG, "GameScreen: MOUSE DOWN........: (" + screenX + "," + screenY + ")" );

        if( button == Input.Buttons.LEFT || button == Input.Buttons.RIGHT ){
            this.setClickedMouseCoordinates(screenX, screenY);
        }

        //left is selection, right is context menu
        if( button == Input.Buttons.LEFT){
            this.selectMouseButtonPressed(screenX, screenY);
        }
        if( button == Input.Buttons.RIGHT){
            this.doActionMouseButtonPressed(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //left is selection, right is context menu
        if( button == Input.Buttons.LEFT){
            this.selectMouseButtonReleased(screenX, screenY);
        }
        if( button == Input.Buttons.RIGHT){
            this.doActionMouseButtonReleased(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void receiveMessage(MESSAGE messageType, Object... args) {

    }

    public void setClickedMouseCoordinates(int x,int y){
        lastMouseCoordinates.set(x, y, 0);
    }

    public void selectMouseButtonPressed(int x, int y){
        mouseButtons.put(Mouse.SELECT, true);
    }

    public void doActionMouseButtonPressed(int x, int y){
        mouseButtons.put(Mouse.DOACTION, true);
    }

    public void selectMouseButtonReleased(int x, int y){
        mouseButtons.put(Mouse.SELECT, false);
    }

    public void doActionMouseButtonReleased(int x, int y){
        mouseButtons.put(Mouse.DOACTION, false);
    }

    public void gotoPosition(Vector2 gotoVector){
        fmState = FmState.GOTO;
        this.gotoVector = gotoVector;
    }
}
