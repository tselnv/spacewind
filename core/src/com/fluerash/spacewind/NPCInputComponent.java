package com.fluerash.spacewind;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;

public class NPCInputComponent extends InputComponent implements InputProcessor {
    private static final String TAG = NPCInputComponent.class.getSimpleName();

    private float frameTime = 0.0f;

    public NPCInputComponent() {
        currentDirection = Entity.Direction.getRandomNext();
        currentState = Entity.State.WALKING;
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
            currentState = Entity.State.getRandomNext();
            currentDirection = Entity.Direction.getRandomNext();
            frameTime = 0.0f;
            entity.setState(currentState);
            entity.setDirection(currentDirection);
        }

        if( currentState == Entity.State.IDLE ){
            //entity.sendMessage(MESSAGE.CURRENT_STATE, Entity.State.IDLE);
            entity.setState(Entity.State.IDLE);
            return;
        }

    }

    @Override
    public boolean keyDown(int keycode) {
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
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
}
