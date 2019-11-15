package com.fluerash.spacewind;


import com.badlogic.gdx.InputProcessor;
import java.util.Map;

import java.util.HashMap;

public abstract class InputComponent implements Component, InputProcessor {
    private static final String TAG = InputComponent.class.getSimpleName();

    protected enum Keys {
        LEFT, RIGHT, UP, DOWN, PAUSE, QUIT
    }

    protected enum Mouse {
        SELECT, DOACTION
    }

    protected static Map<Keys, Boolean> keys = new HashMap<>();
    protected static Map<Mouse, Boolean> mouseButtons = new HashMap<>();

    //initialize the hashmap for inputs
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.QUIT, false);
        keys.put(Keys.PAUSE, false);
    };
    static {
        mouseButtons.put(Mouse.SELECT, false);
        mouseButtons.put(Mouse.DOACTION, false);
    };

    protected Entity.Direction currentDirection = null;
    protected Entity.State currentState = null;

    public abstract void update(Entity entity, com.fluerash.spacewind.maps.Map map, float delta);

    public void setState(Entity.State state) {
        this.currentState = state;
    }

    public void setDirection(Entity.Direction direction) {
        this.currentDirection = direction;
    }

    public void collisionWithMap(){
        currentDirection = Entity.Direction.getRandomNext();
    }
}
