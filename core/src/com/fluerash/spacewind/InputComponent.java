package com.fluerash.spacewind;


import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;
import java.util.Map;

public abstract class InputComponent implements Component, InputProcessor {
    private static final String TAG = InputComponent.class.getSimpleName();

    protected enum Keys {
        LEFT, RIGHT, UP, DOWN, PAUSE, QUIT
    }

    protected static Map<Keys, Boolean> keys = new HashMap<Keys,Boolean>();
    //initialize the hashmap for inputs
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.QUIT, false);
        keys.put(Keys.PAUSE, false);
    };

    protected Entity.Direction currentDirection = null;
    protected Entity.State currentState = null;

    public abstract void update(Entity entity, float delta);

    public void setState(Entity.State state) {
        this.currentState = state;
    }

    public void setDirection(Entity.Direction direction) {
        this.currentDirection = direction;
    }
}
